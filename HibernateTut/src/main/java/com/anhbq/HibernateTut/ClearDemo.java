package com.anhbq.HibernateTut;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.anhbq.HibernateTut.model.Department;
import com.anhbq.HibernateTut.model.Employee;
import com.anhbq.HibernateTut.util.DataUtil;

public class ClearDemo {
	public static void main(String[] args) {
		SessionFactory factory = HibernateUtil.getSessionFactory();

		Session session = factory.getCurrentSession();
		Employee emp = null;
		Department dept = null;
		try {
			session.getTransaction().begin();

			// Đây là một đối tượng có tình trạng Persistent.
			emp = DataUtil.findEmployee(session, "E7499");
			dept = DataUtil.findDepartment(session, "D10");

			// Sử dụng clear để đuổi hết tất cả các đối tượng có
			// trạng thái Persistent ra khỏi sự quản lý của Hibernate.
			session.clear();

			// Lúc này 'emp' & 'dept' đang có trạng thái Detached.
			// ==> false
			System.out.println("- emp Persistent? " + session.contains(emp));
			System.out.println("- dept Persistent? " + session.contains(dept));

			// Tất cả các thay đổi trên 'emp' sẽ không được update
			// nếu không đưa 'emp' trở lại trạng thái Persistent.
			emp.setEmpNo("NEW");

			dept = DataUtil.findDepartment(session, "D20");
			System.out.println("Dept Name = " + dept.getDeptName());

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}
}
