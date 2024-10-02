package ru.hogwarts.school.exception;

public class AvatarByStudentIdNotFoundException extends RuntimeException{
    public AvatarByStudentIdNotFoundException() {
        super("ID студента не найден");
    }
}
