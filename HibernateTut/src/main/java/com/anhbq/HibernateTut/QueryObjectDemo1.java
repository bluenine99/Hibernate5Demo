package com.anhbq.HibernateTut;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.anhbq.HibernateTut.model.Employee;

public class QueryObjectDemo1 {
	public static void main(String[] args) {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			// Tất cả các lệnh hành động với DB thông qua Hibernate
			// đều phải nằm trong 1 giao dịch (Transaction)
			// Bắt đầu giao dịch
			session.getTransaction().begin();

			// Tạo một câu lệnh HQL query object.
			// HQL Có tham số.
			// Tương đương với Native SQL:
			// Select e.* from EMPLOYEE e cross join DEPARTMENT d
			// where e.DEPT_ID = d.DEPT_ID and d.DEPT_NO = :deptNo;

			String sql = "Select e from " + Employee.class.getName() + " e " + " where e.department.deptNo=:deptNo ";

			// Tạo đối tượng Query.
			Query<Employee> query = session.createQuery(sql);

			query.setParameter("deptNo", "D10");

			// Thực hiện truy vấn.
			List<Employee> employees = query.getResultList();

			for (Employee emp : employees) {
				System.out.println("Emp: " + emp.getEmpNo() + " : " + emp.getEmpName());
			}

			// Commit dữ liệu
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			// Rollback trong trường hợp có lỗi xẩy ra.
			session.getTransaction().rollback();
		}

	}
}
