package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
class Visitor {
    String name;
    String surname;
    String phone;
    Boolean subscribed;
    Book[] favoriteBooks;
}

@AllArgsConstructor
@Getter
@Setter
@ToString
class Book {
    String name;
    String author;
    int publishingYear;
    String isbn;
    String publisher;
}

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        List<Visitor> visitors = new LinkedList<>();

        try (FileReader fileReader = new FileReader("src/main/resources/books.json")) {
            Type visitorList = new TypeToken<List<Visitor>>(){}.getType();

            visitors = gson.fromJson(fileReader, visitorList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        // Задание 1
        visitors.stream().forEach(System.out::println); // вывел всех посетителей
        System.out.println(visitors.stream().count()); // их количество

        // Задание 2

    }
}