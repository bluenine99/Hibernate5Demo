package com.anhbq.HibernateTut;

import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.anhbq.HibernateTut.model.Department;
import com.anhbq.HibernateTut.model.Employee;
import com.anhbq.HibernateTut.util.DataUtil;

public class PersistDemo {
	public static void main(String[] args) {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		Department department = null;
		Employee emp = null;
		try {
			session.getTransaction().begin();

			Long maxEmpId = DataUtil.getMaxEmpId(session);
			Long empId = maxEmpId + 1;

			// Phòng ban với mã số D10.
			// Nó là đối tượng chịu sự quản lý của session
			// Và được gọi là đối tượng persistent.

			department = DataUtil.findDepartment(session, "D10");

			// Tạo mới đối tượng Employee
			// Đối tượng này chưa chịu sự quản lý của session.
			// Nó được coi là đối tượng Transient.

			emp = new Employee();
			emp.setEmpId(empId);
			emp.setEmpNo("E" + empId);
			emp.setEmpName("Name " + empId);
			emp.setJob("Coder");
			emp.setSalary(1000f);
			emp.setManager(null);
			emp.setHideDate(LocalDate.now());
			emp.setDepartment(department);

			// Sử dụng persist(..)
			// Lúc này 'emp' đã chịu sự quản lý của session.
			// nó có trạng thái persistent.
			// Chưa có hành động gì với DB tại đây.

			session.persist(emp);

			// Tại bước này dữ liệu mới được đẩy xuống DB.
			// Câu lệnh Insert được tạo ra.

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}

		// Sau khi session bị đóng lại (commit, rollback, close)
		// Đối tượng 'emp', 'dept' trở thành đối tượng Detached.
		// Nó không còn trong sự quản lý của session nữa.

		System.out.println("Emp No: " + emp.getEmpNo());
	}
}
