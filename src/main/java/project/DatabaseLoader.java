package project;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoader {


	@Autowired
	private final StudentRepository studentRepository;

//	@Autowired
//	public DatabaseLoader(StudentRepository studentRepository) {
//
//		this.studentRepository = studentRepository;
//	}


	@PostConstruct
	private void initDatabase() {
		studentRepository.deleteAll();

		Student roy = new Student("Roy", "Clarkson");
		roy.setGrade("1st base");
		studentRepository.save(roy);

		Student phil = new Student("Phil", "Webb");
		phil.setGrade("2nd ");
		studentRepository.save(phil);
	}

}
