package org.gzlabs.jpa.explode;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public interface GenericEntity extends Serializable {


   /**
    * Get entity id
    *
    * @return
    */
   public abstract Number getId();

    /**
     * Explode entity (default indexDeep to 1) query values ​​with related entities
     *
     * @param indexDeep this value indicates the indexDeep factor on which search the JPA entity
     * @param includeList this parameter indicates whether the lists are included when making a explode (false by default)
     *
     * @return entity object with calculated entities
     * 
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public GenericEntity explode(Integer indexDeep, Boolean includeList) throws NoSuchMethodException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, Exception;

    
		/**
		 * Set to NULL all fields with @NotVisible annotation
		 *
		 * @return entity object without not visible fields
		 * 
		 * @throws NoSuchMethodException
		 * @throws IllegalArgumentException
		 * @throws SecurityException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 * @throws InstantiationException
		 * @throws ClassNotFoundException
		 * @throws Exception
		 */		
		public <T extends GenericEntity> T applyNotVisible() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, Exception;

    /**
     * Explode entity (default indexDeep to 1) query values ​​with related entities
     *
     * @param indexDeep this value indicates the indexDeep factor on which search the JPA entity
     *
     * @return entity object with calculated entities
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public GenericEntity explode(Integer indexDeep) throws NoSuchMethodException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, Exception;
    
}