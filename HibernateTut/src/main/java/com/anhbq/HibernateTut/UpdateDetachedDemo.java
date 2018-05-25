package com.anhbq.HibernateTut;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.anhbq.HibernateTut.model.Employee;
import com.anhbq.HibernateTut.util.DataUtil;

public class UpdateDetachedDemo {
	public static void main(String[] args) {
		SessionFactory factory = HibernateUtil.getSessionFactory();

		Session session1 = factory.getCurrentSession();
		Employee emp = null;
		try {
			session1.getTransaction().begin();

			// Đây là đối tượng có trạng thái Persistent.
			emp = DataUtil.findEmployee(session1, "E7499");

			// session1 đã bị đóng lại sau commit được gọi.
			session1.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session1.getTransaction().rollback();
		}

		// Mở một Session khác
		Session session2 = factory.getCurrentSession();

		try {
			session2.getTransaction().begin();

			// Kiểm tra trạng thái của emp:
			// ==> false
			System.out.println("- emp Persistent? " + session2.contains(emp));

			System.out.println("Emp salary: " + emp.getSalary());

			emp.setSalary(emp.getSalary() + 100);

			// update(..) chỉ áp dụng cho đối tượng Detached.
			// (Không dùng được với đối tượng Transient).
			// Sử dụng update(emp) để đưa emp trở lại trạng thái Persistent.
			session2.update(emp);

			// Chủ động đẩy dữ liệu xuống DB.
			// Câu lệnh update sẽ được gọi.
			session2.flush();

			System.out.println("Emp salary after update: " + emp.getSalary());

			// session2 đã bị đóng lại sau commit được gọi.
			session2.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session2.getTransaction().rollback();
		}

	}

}
