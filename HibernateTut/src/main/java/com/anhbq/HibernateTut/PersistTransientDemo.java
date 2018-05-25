package com.anhbq.HibernateTut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.anhbq.HibernateTut.model.Employee;
import com.anhbq.HibernateTut.model.Timekeeper;
import com.anhbq.HibernateTut.util.DataUtil;

public class PersistTransientDemo {
	private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private static Timekeeper persist_Transient(Session session, Employee emp) {
		// Hãy chú ý:
		// timekeeperId cấu hình tự động được tạo ra bởi UUID.
		// @GeneratedValue(generator = "uuid")
		// @GenericGenerator(name = "uuid", strategy = "uuid2")
		// Tạo một đối tượng, nó đang có tình trạng Transient.

		Timekeeper tk1 = new Timekeeper();

		tk1.setEmployee(emp);
		tk1.setInOut(Timekeeper.IN);
		tk1.setDateTime(LocalDate.now());

		// Now, 'tk1' is transient object
		System.out.println("- tk1 Persistent? " + session.contains(tk1));

		System.out.println("====== CALL persist(tk).... ===========");

		// Hibernate gán Id vào 'tk1', sẽ chưa có insert gì cả

		session.persist(tk1);

		// 'tk1' đã được gắn ID
		System.out.println("- tk1.getTimekeeperId() = " + tk1.getTimekeeperId());

		// Lúc này 'tk1' đã có trạng thái Persistent
		// Nó đã được quản lý trong Session.
		// Nhưng chưa có hành động gì insert xuống DB.
		// ==> true

		System.out.println("- tk1 Persistent? " + session.contains(tk1));

		System.out.println("- Call flush..");

		// Chủ động đẩy dữ liệu xuống DB, gọi flush().
		// Nếu không gọi flush() dữ liệu sẽ được đẩy xuống tại lệnh commit().
		// Lúc này mới có insert.

		session.flush();

		String timekeeperId = tk1.getTimekeeperId();
		System.out.println("- timekeeperId = " + timekeeperId);
		System.out.println("- inOut = " + tk1.getInOut());
		System.out.println("- dateTime = " + df.format(tk1.getDateTime()));
		System.out.println();
		return tk1;
	}

	public static void main(String[] args) {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		Employee emp = null;
		try {
			session.getTransaction().begin();

			emp = DataUtil.findEmployee(session, "E7499");

			persist_Transient(session, emp);

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}

}
