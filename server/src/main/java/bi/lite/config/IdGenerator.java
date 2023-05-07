package bi.lite.config;

import bi.lite.util.Snowflake53;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * @author lipengpeng
 */
public class IdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return Snowflake53.nextId();
    }
}
