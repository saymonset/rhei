package ve.org.bcv.rhei.report.by.benef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Student {
	private String name;
	private String roll_no;
	private String fathers_name;
	private String studying_in;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoll_no() {
		return roll_no;
	}

	public void setRoll_no(String roll_no) {
		this.roll_no = roll_no;
	}

	public String getFathers_name() {
		return fathers_name;
	}

	public void setFathers_name(String fathers_name) {
		this.fathers_name = fathers_name;
	}

	public String getStudying_in() {
		return studying_in;
	}

	public void setStudying_in(String studying_in) {
		this.studying_in = studying_in;
	}

	public static Collection getStudentList() {
		List students = new ArrayList();
		try {
			Student student = new Student();
			student.setRoll_no("101");
			student.setName("Steve");
			student.setFathers_name("Jack");
			student.setStudying_in("I-A");
			students.add(student);
			student = new Student();
			student.setRoll_no("102");
			student.setName("Mark");
			student.setFathers_name("Henry");
			student.setStudying_in("I-A");
			students.add(student);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return students;
	}
}