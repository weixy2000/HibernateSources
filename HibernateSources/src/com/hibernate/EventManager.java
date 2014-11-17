package com.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

public class EventManager {

    public static void main(String[] args) {
    	String what = "store";
    	
        EventManager mgr = new EventManager();
        if (what.equals("store")){
            mgr.createAndStoreEvent("My Event222", new Date());
        }else if (what.equals("list")){
            List events = mgr.listEvents();
            for (int i = 0; i < events.size(); i++){
                Event theEvent = (Event) events.get(i);
                System.out.println("Event: " + theEvent.getTitle() + " Time: " + theEvent.getDate());
            }
        }else if(what.equals("store1")){
        	mgr.createAndStorePerson(20, "firstname1", "lastname1");
        }else if(what.equals("add")){
        	mgr.addPersonToEvent(1L, 2L);
        }else if(what.equals("add1")){
        	mgr.addPersonToEvent1(1L, 1L);
        }else if(what.equals("addEmail")){
        	mgr.addEmailToPerson(1L, "872254558@qq.com");
        }
        HibernateUtil.getSessionFactory().close();
    }

    private Long createAndStoreEvent(String title, Date theDate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();//获取Session
        session.beginTransaction();//启动事务

        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        session.save(theEvent);//保存对象
        
        session.getTransaction().commit();//提交事务
        return theEvent.getId();
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
	private List listEvents() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();//获取Session
        session.beginTransaction();//启动事务
        List result = session.createQuery("from Event").list();//执行查询
        session.getTransaction().commit();//提交事务
        return result;
    }
    
    private Long createAndStorePerson(int age, String firstname, String lastname) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();//获取Session
        session.beginTransaction();//启动事务
        
        Person person = new Person();
        person.setAge(age);
        person.setFirstname(firstname);
        person.setLastname(lastname);
        session.save(person);//保存对象
        
        session.getTransaction().commit();//提交事务
        return person.getId();
    }

    /**
     * 持久化的
     * 
     * @param personId
     * @param eventId
     */
    private void addPersonToEvent(Long personId, Long eventId) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);
        Event anEvent = (Event) session.load(Event.class, eventId);

        aPerson.getEvents().add(anEvent);

        session.getTransaction().commit();
    }

    /**
     * 托管的
     * 
     * @param personId
     * @param eventId
     */
    private void addPersonToEvent1(Long personId, Long eventId) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session
                .createQuery("select p from Person p left join fetch p.events where p.id = :pid")
                .setParameter("pid", personId)
                .uniqueResult(); // Eager fetch the collection so we can use it detached

        Event anEvent = (Event) session.load(Event.class, eventId);

        session.getTransaction().commit();

        // 结束第一个工作单元

        aPerson.getEvents().add(anEvent); // aPerson (and its collection) is detached

        // Begin second unit of work

        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        session2.beginTransaction();

        session2.update(aPerson); // Reattachment of aPerson

        session2.getTransaction().commit();
    }
    
    /**
     * 添加EMAIL_ADDR
     * 
     * @param personId
     * @param emailAddress
     */
    private void addEmailToPerson(Long personId, String emailAddress) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);

        // The getEmailAddresses() might trigger a lazy load of the collection
        aPerson.getEmailAddresses().add(emailAddress);

        session.getTransaction().commit();
    }

}