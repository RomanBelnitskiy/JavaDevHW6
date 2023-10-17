package org.example;

import org.example.config.Database;
import org.example.service.ClientService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = new ClientService(Database.getConnection());
        long id = clientService.create("Express");
        System.out.println("id = " + id);

        String name = clientService.getById(id);
        System.out.println("name = " + name);

        clientService.setName(id, "AliExpress");
        name = clientService.getById(id);
        System.out.println("name = " + name);

        clientService.deleteById(id);

        printList(clientService.listAll());

        Database.getInstance().closeConnection();
    }

    private static <T> void printList(List<T> list) {
        System.out.println();
        System.out.println("==============================================");
        for (T element : list) {
            System.out.println(element);
        }
        System.out.println("==============================================");
        System.out.println();
    }
}