package repository;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {

    private static final List<User> users = new ArrayList<>();

    public static void save(User user){
        users.add(user);
    }

    public List<User> findByGame(String game){
        return users.stream()
                .filter(user -> user.getGame().equalsIgnoreCase(game))
                .collect(Collectors.toList());
    }
}
