package com.bolun.hotel.util;

import com.bolun.hotel.converter.BirthdateConverter;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(UserDetail.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(Apartment.class);
        configuration.addAttributeConverter(new BirthdateConverter(), true);
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
