package project;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Link;

public class StudentAndLink {

    private final Student student;
    private final Link link;

    public StudentAndLink(Student student) {
        this.student = student;
        this.link = linkTo(methodOn(StudentController.class)
                .getStudent(student.getId()))
                .withRel(student.getFirstName() + " " + student.getLastName());
    }

    public Student getStudent() {
        return student;
    }

    public Link getLink() {
        return link;
    }
}
