package com.anhbq.HibernateTut;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.anhbq.HibernateTut.model.Employee;
import com.anhbq.HibernateTut.util.DataUtil;

public class RefreshDetachedDemo {
	public static void main(String[] args) {

		// Một đối tượng có trạng thái Detached.
		Employee emp = getEmployee_Detached();

		System.out.println(" - GET EMP " + emp.getEmpId());

		// refresh đối tượng Detached.
		refresh_test(emp);
	}

	// Hàm trả về một đối tượng Employee đã
	// nằm ngoài sự quản lý của Hibernate (Detached).
	private static Employee getEmployee_Detached() {
		SessionFactory factory = HibernateUtil.getSessionFactory();

		Session session1 = factory.getCurrentSession();
		Employee emp = null;
		try {
			session1.getTransaction().begin();

			emp = DataUtil.findEmployee(session1, "E7839");

			// session1 đã bị đóng lại sau commit được gọi.
			// Một bản ghi Employee đã được insert vào DB.
			session1.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session1.getTransaction().rollback();
		}
		// session1 đã bị đóng 'emp' đã trở về trạng thái Detached.
		return emp;
	}

	private static void refresh_test(Employee emp) {
		SessionFactory factory = HibernateUtil.getSessionFactory();

		// Mở một Session khác

		Session session2 = factory.getCurrentSession();

		try {
			session2.getTransaction().begin();

			// Thực tế emp đang có trạng thái Detached
			// Nó không được quản lý bởi Hibernate.
			// Kiểm tra trạng thái của emp:
			// ==> false
			System.out.println(" - emp Persistent? " + session2.contains(emp));

			System.out.println(" - Emp salary before update: " + emp.getSalary());

			// Set salary mới cho đối tượng Detached emp.
			emp.setSalary(emp.getSalary() + 100);

			// refresh tạo ra câu query.
			// Và đưa đối tượng Detached về Persistent.
			// Các thay đổi của emp không được lưu lại.O
			session2.refresh(emp);

			// ==> true
			System.out.println(" - emp Persistent? " + session2.contains(emp));

			System.out.println(" - Emp salary after refresh: " + emp.getSalary());

			session2.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session2.getTransaction().rollback();
		}

	}
}
