package project;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StudentController {//student controller

    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public ModelAndView getStudents() {
        // Specify the view name
        return new ModelAndView("students")


                .addObject("students",
                        StreamSupport.stream(studentRepository.findAll().spliterator(),false)
                        .map(StudentAndLink::new)
                        .toArray()

                        )



                .addObject("student", new Student())
                .addObject("postLink",
                        linkTo(methodOn(StudentController.class).newStudent(null))
                                .withRel("Create"))
                .addObject("links", Arrays.asList(
                        linkTo(methodOn(StudentController.class).getStudents())
                                .withRel("All Students")
                ));
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/students", method = RequestMethod.POST)
    public ModelAndView newStudent(@ModelAttribute Student student) {//create new

        studentRepository.save(student);

        return getStudents();
    }


    /**
     * get one student
     * @param id
     * @return
     */

    @Secured("ROLE_USER")
    @RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
    public ModelAndView getStudent(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("student");

        final Student student = studentRepository.findOne(id);
        modelAndView.addObject("student", student);

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(StudentController.class).getStudents())
                .withRel("All Students"));

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ROLE_ADMIN"))) {
            links.add(linkTo(methodOn(StudentController.class).editStudent(id))
                    .withRel("Edit"));
        }

        modelAndView.addObject("links", links);
        return modelAndView;
    }

    /**
     * edit
     * @param id
     * @param student
     * @return
     */


    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/student/{id}", method = RequestMethod.PUT)
    public ModelAndView updateStudent(@PathVariable Long id, @ModelAttribute Student student) {

        student.setId(id);
        studentRepository.save(student);

        return getStudent(student.getId());
    }

    /**
     * eidt one student
     * @param id
     * @return
     */


    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/student/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editStudent(@PathVariable Long id) {
        final Student student = studentRepository.findOne(id);
        return new ModelAndView("edit")
                .addObject("student", student)
                .addObject("putLink",
                        linkTo(methodOn(StudentController.class).updateStudent(id, student))
                                .withRel("Update"))
                .addObject("links", Arrays.asList(
                        linkTo(methodOn(StudentController.class).getStudent(id))
                                .withRel("Cancel")
                ));
    }

}
