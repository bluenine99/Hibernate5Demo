package com.anhbq.HibernateTut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.anhbq.HibernateTut.model.Employee;
import com.anhbq.HibernateTut.model.Timekeeper;
import com.anhbq.HibernateTut.util.DataUtil;

public class SaveOrUpdateTransientDemo {
	private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private static Timekeeper saveOrUpdate_Transient(Session session, Employee emp) {
		// Hãy chú ý:
		// timekeeperId cấu hình tự động được tạo ra bởi UUID.
		// @GeneratedValue(generator = "uuid")
		// @GenericGenerator(name = "uuid", strategy = "uuid2")
		// Tạo một đối tượng, nó đang có tình trạng Transient.
		Timekeeper tk3 = new Timekeeper();

		tk3.setEmployee(emp);
		tk3.setInOut(Timekeeper.IN);
		tk3.setDateTime(LocalDate.now());

		// Lúc này 'tk3' đang có tình trạng Transient.
		System.out.println("- tk3 Persistent? " + session.contains(tk3));

		System.out.println("====== CALL saveOrUpdate(tk).... ===========");

		// Tại đây Hibernate sẽ kiểm tra, tk3 có ID chưa (timekeeperId)
		// Nếu chưa có nó tự gán ID vào.
		session.saveOrUpdate(tk3);

		System.out.println("- tk3.getTimekeeperId() = " + tk3.getTimekeeperId());

		// Lúc này 'tk3' đã có trạng thái Persistent
		// Nó đã được quản lý trong Session.
		// Nhưng chưa có hành động gì insert, hoặc update xuống DB.
		// ==> true
		System.out.println("- tk3 Persistent? " + session.contains(tk3));

		System.out.println("- Call flush..");
		// Chủ động đẩy dữ liệu xuống DB, gọi flush().
		// Nếu không gọi flush() dữ liệu sẽ được đẩy xuống tại lệnh commit().
		// Lúc này có thể có Insert hoặc Update xuống DB. (!!!)
		// Tùy thuộc vào ID của 'tk3' có trên DB chưa.
		session.flush();

		String timekeeperId = tk3.getTimekeeperId();
		System.out.println("- timekeeperId = " + timekeeperId);
		System.out.println("- inOut = " + tk3.getInOut());
		System.out.println("- dateTime = " + df.format(tk3.getDateTime()));
		System.out.println();
		return tk3;
	}

	public static void main(String[] args) {
		SessionFactory factory = HibernateUtil.getSessionFactory();

		Session session = factory.getCurrentSession();
		Employee emp = null;
		try {
			session.getTransaction().begin();

			emp = DataUtil.findEmployee(session, "E7499");

			saveOrUpdate_Transient(session, emp);

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}

}
