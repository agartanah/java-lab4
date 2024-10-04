package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.*;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class Visitor {
    private String name;
    private String surname;
    private String phone;
    private Boolean subscribed;
    private List<Book> favoriteBooks;
}

@Data
@AllArgsConstructor
class Book {
    String name;
    String author;
    int publishingYear;
    String isbn;
    String publisher;
}

@Data
@AllArgsConstructor
class Sms {
    String phoneNumber;
    String message;
}

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        List<Visitor> visitors;

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
        List<String> books = visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .map(Book::getName)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(books);
        System.out.println(books.size());

        // Задание 3
        visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .sorted(Comparator.comparing(Book::getPublishingYear))
                .forEach(System.out::println);

        // Задание 4
        boolean isJane = visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .map(book -> book.author)
                .anyMatch(Predicate.isEqual("Jane Austen"));
        if (isJane) {
            System.out.println("Visitors have ordered a Jane Austen book at least once");
        }

        // Задание 5
        System.out.println(visitors.stream()
                .map(visitor -> visitor.getFavoriteBooks().size())
                .max(Comparator.naturalOrder()));

        // Задание 6
        double avgCountBooks = visitors.stream()
                .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                .average()
                .orElse(0);

        visitors.stream()
                .filter(Visitor::getSubscribed)
                .map(visitor -> {
                    int countBooks = visitor.getFavoriteBooks().size();

                    if (countBooks > avgCountBooks) {
                        return new Sms(visitor.getPhone(), "you are a bookworm");
                    } else if (countBooks == avgCountBooks) {
                        return new Sms(visitor.getPhone(), "fine");
                    } else {
                        return new Sms(visitor.getPhone(), "read more");
                    }
                })
                .forEach(System.out::println);
    }
}