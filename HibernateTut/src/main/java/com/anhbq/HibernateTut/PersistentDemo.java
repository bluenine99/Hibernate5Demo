package com.anhbq.HibernateTut;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.anhbq.HibernateTut.model.Department;
import com.anhbq.HibernateTut.util.DataUtil;

public class PersistentDemo {

	public static void main(String[] args) {
		SessionFactory factory = HibernateUtil.getSessionFactory();

		Session session = factory.getCurrentSession();
		Department department = null;
		try {
			session.getTransaction().begin();

			System.out.println("- Finding Department deptNo = D10...");
			// Đây là một đối tượng có trạng thái Persistent.
			department = DataUtil.findDepartment(session, "D10");

			System.out.println("- First change Location");
			// Thay đổi gì đó trên đối tượng Persistent.
			department.setLocation("Chicago " + System.currentTimeMillis());

			System.out.println("- Location = " + department.getLocation());

			System.out.println("- Calling flush...");
			// Sử dụng session.flush() để chủ động đẩy các thay đổi xuống DB.
			// Có tác dụng trên tất cả các đối tượng Persistent có thay đổi.
			session.flush();

			System.out.println("- Flush OK");

			System.out.println("- Second change Location");
			// Thay đổi gì đó trên đối tượng Persistent.
			// Hình thành câu lệnh update, sẽ được thực thi
			// sau khi session đóng lại (commit).

			department.setLocation("Chicago " + System.currentTimeMillis());

			// In ra Location.
			System.out.println("- Location = " + department.getLocation());

			System.out.println("- Calling commit...");

			// Lệnh commit sẽ làm tất cả những sự thay đổi được đẩy xuống DB.

			session.getTransaction().commit();

			System.out.println("- Commit OK");
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}

		// Tạo lại session sau khi nó đã bị đóng trước đó
		// (Do commit hoặc rollback)

		session = factory.getCurrentSession();
		try {
			session.getTransaction().begin();

			System.out.println("- Finding Department deptNo = D10...");

			// Query lại Department D10.

			department = DataUtil.findDepartment(session, "D10");

			// In ra thông tin Location.

			System.out.println("- D10 Location = " + department.getLocation());

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}

	}

}
