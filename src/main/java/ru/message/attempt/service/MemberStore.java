package ru.message.attempt.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.message.attempt.model.User;

@Service
public class MemberStore {

    private static List<User> store = new LinkedList<>();

    public List<User> getMembersList() {
        AtomicInteger serialId = new AtomicInteger(1);
        return store.stream()
                .map(user -> new User(user.id(), serialId.getAndIncrement() + "", user.username()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<User> filterMemberListByUser(List<User> memberList, User user) {
        return memberList.stream()
                .filter(filterUser -> !Objects.equals(filterUser.id(), user.id()))
                .map(sendUser -> new User(null, sendUser.serialId(), sendUser.username()))
                .toList();
    }

    public User getMember(String id) {
        return store.get(Integer.parseInt(id) - 1);
    }

    public void addMember(User member) {
        store.add(member);
    }

    public void removeMember(User member) {
        store.remove(member);
    }
}