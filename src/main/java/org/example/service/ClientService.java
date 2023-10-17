package org.example.service;

import org.example.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ClientService {
    private final String CREATE_SQL = "INSERT INTO client(NAME) VALUES(?)";
    private final String FIND_BY_ID_SQL = "SELECT ID, NAME FROM client WHERE ID = ?";
    private final String UPDATE_NAME_SQL = "UPDATE client SET NAME = ? WHERE ID = ?";
    private final String DELETE_SQL = "DELETE FROM client WHERE ID = ?";
    private final String FIND_ALL_SQL = "SELECT ID, NAME FROM client";

    private PreparedStatement createStatement;
    private PreparedStatement findByIdStatement;
    private PreparedStatement updateNameStatement;
    private PreparedStatement deleteStatement;
    private PreparedStatement findAllStatement;

    public ClientService(Connection connection) {
        try {
            createStatement = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);
            findByIdStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            updateNameStatement = connection.prepareStatement(UPDATE_NAME_SQL);
            deleteStatement = connection.prepareStatement(DELETE_SQL);
            findAllStatement = connection.prepareStatement(FIND_ALL_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long create(String name) {
        validateName(name);

        try {
            createStatement.setString(1, name);

            int affectedRows = createStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Creating client failed, no rows affected.");
            }

            try (ResultSet generatedKeys = createStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                else {
                    throw new RuntimeException("Creating client failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getById(long id) {
        validateId(id);

        try {
            findByIdStatement.setLong(1, id);

            ResultSet resultSet = findByIdStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString(2);
                resultSet.close();

                return name;
            } else {
                throw new RuntimeException("Can't find client with id=" + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(long id, String name) {
        validateId(id);
        validateName(name);

        try {
            updateNameStatement.setString(1, name);
            updateNameStatement.setLong(2, id);

            int affectedRows = updateNameStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException(String.format("Client with id=%s does not exist", id));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(long id) {
        validateId(id);

        try {
            deleteStatement.setLong(1, id);

            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Client> listAll() {
        try {
            ResultSet resultSet = findAllStatement.executeQuery();

            List<Client> clients = new ArrayList<>();

            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                String name = resultSet.getString(2);

                clients.add(new Client(id, name));
            }
            resultSet.close();

            return clients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateName(String name) {
        requireNonNull(name);
        if (name.length() < 2) {
            throw new RuntimeException("Client name length should be greater than 1 character");
        }
        if (name.length() > 255) {
            throw new RuntimeException("Client name length should be smaller than 256 characters");
        }
    }

    private void validateId(long id) {
        if (id < 1) {
            throw new RuntimeException("Client id can't be less than 1");
        }
    }
}
