package com.anhbq.HibernateTut;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.anhbq.HibernateTut.model.Employee;
import com.anhbq.HibernateTut.util.DataUtil;

public class EvictDemo {
	public static void main(String[] args) {
		SessionFactory factory = HibernateUtil.getSessionFactory();

		Session session = factory.getCurrentSession();
		Employee emp = null;
		try {
			session.getTransaction().begin();

			// Đây là một đối tượng có tình trạng Persistent.
			emp = DataUtil.findEmployee(session, "E7499");

			// ==> true
			System.out.println("- emp Persistent? " + session.contains(emp));

			// Sử dụng evict(Object) để đuổi đối tượng Persistent
			// ra khỏi quản lý của Hibernate.
			session.evict(emp);

			// Lúc này 'emp' đang có trạng thái Detached.
			// ==> false
			System.out.println("- emp Persistent? " + session.contains(emp));

			// Tất cả các thay đổi trên 'emp' sẽ không được update
			// nếu không đưa 'emp' trở lại trạng thái Persistent.
			emp.setEmpNo("NEW");

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}
}
