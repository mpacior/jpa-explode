package org.gzlabs.jpa.explode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.gzlabs.jpa.explode.annotation.NotVisible;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;


@SuppressWarnings("serial")
public abstract class GenericEntityBean implements GenericEntity {

		public GenericEntityBean() {
		}

		@Override
		public abstract Number getId();

		/**
		 * Explode entity (default indexDeep to 1) query values ​​with related entities
		 *
		 * @param indexDeep this value indicates the indexDeep factor on which search the JPA entity
		 * @param includeList this parameter indicates whether the lists are included when making a explode (false by default)
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
		@SuppressWarnings("rawtypes")
		@Override
		public GenericEntity explode(Integer indexDeep, Boolean includeList) throws NoSuchMethodException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, Exception {
				try {
						List<Field> fields = GenericEntityBean.getAllFields(new ArrayList<Field>(), this.getClass());
						// System.out.println("[indexDeep vale]: " + indexDeep);
						if ((fields != null) && (!fields.isEmpty())) {
								for (Field selectField : fields) {
										selectField.setAccessible(true);
										NotVisible isNotVisible = selectField.getAnnotation(NotVisible.class);
										if (((GenericEntity.class.isAssignableFrom(selectField.getType()))) && (isNotVisible == null)) {
												String nameGetMethod = "get" + selectField.getName().substring(0, 1).toUpperCase() + selectField.getName().substring(1);
												Method getMethod = this.getClass().getMethod(nameGetMethod);
												if ((indexDeep > 0) && (getMethod != null)) {
														if (getMethod.invoke(this) != null) {
																selectField.set(this, ((GenericEntityBean) getMethod.invoke(this)).explode(indexDeep - 1));
														}
												}
										} else if ((includeList) && (Collection.class.isAssignableFrom(selectField.getType())) && (isNotVisible == null)) {
												if ((indexDeep > 0) && (selectField.get(this) != null)) {
														Collection collection = (Collection) selectField.get(this);
														Iterator items = collection.iterator();
														while (items != null && items.hasNext()) {
																Object nextItem = items.next();
																if (nextItem != null) {
																		((GenericEntityBean) nextItem).explode(indexDeep - 1);
																}
														}
												}
										} else if ((!includeList) && (Collection.class.isAssignableFrom(selectField.getType())) && (isNotVisible == null)) {
												// selectField.set(this, null);
										} else if (isNotVisible != null) {
												selectField.set(this, null);
										}
								}
						}
						return this;
				} catch (IllegalArgumentException e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new IllegalArgumentException(e);
				} catch (SecurityException e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new SecurityException(e);
				} catch (IllegalAccessException e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new IllegalAccessException(e.getMessage());
				} catch (NoSuchMethodException e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new NoSuchMethodException(e.getMessage());
				} catch (Exception e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new Exception(e);
				}
		}

		/**
		 * Set to NULL all fields with @NotVisible annotation
		 *
		 * @return entity object without not visible fields
		 * 
		 * @throws IllegalArgumentException
		 * @throws SecurityException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 * @throws InstantiationException
		 * @throws ClassNotFoundException
		 * @throws Exception
		 */
		@Override
		public GenericEntity applyNotVisible() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, Exception {
				try {
						List<Field> fields = GenericEntityBean.getAllFields(new ArrayList<Field>(), this.getClass());
						if ((fields != null) && (!fields.isEmpty())) {
								for (Field selectField : fields) {
										selectField.setAccessible(true);
										NotVisible isNotVisible = selectField.getAnnotation(NotVisible.class);
										if (isNotVisible != null) {
												selectField.set(this, null);
										}
								}
						}
						return this;
				} catch (IllegalArgumentException e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new IllegalArgumentException(e);
				} catch (SecurityException e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new SecurityException(e);
				} catch (IllegalAccessException e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new IllegalAccessException(e.getMessage());
				} catch (Exception e) {
						LoggerFactory.getLogger(getClass()).error(e.getMessage());
						throw new Exception(e);
				}
		}

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
		@Override
		public GenericEntity explode(Integer indexDeep) throws NoSuchMethodException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, Exception {
				return explode(indexDeep, false);
		}

		/**
		 * Explode entity list (default indexDeep to 1) query values ​​with related entities
		 *
		 * @param <T extends GenericEntityBean>
		 * @param entityList
		 * @param indexDeep
		 * @return
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 * @throws InstantiationException
		 * @throws SecurityException
		 * @throws ClassNotFoundException
		 * @throws Exception
		 */
		public static <T extends GenericEntityBean> List<T> explodeList(List<T> entityList, Integer indexDeep) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, SecurityException, ClassNotFoundException, Exception {
				if ((entityList != null) && (!entityList.isEmpty())) {
						for (int i = 0; i < entityList.size(); i++) {
								entityList.get(i).explode(indexDeep);
						}
				}
				return entityList;
		}

		/**
		 * Explode entity list (default indexDeep to 1) query values ​​with related entities
		 *
		 * @param <T extends GenericEntityBean>
		 * @param entityPage
		 * @param indexDeep
		 * @return
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 * @throws InstantiationException
		 * @throws SecurityException
		 * @throws ClassNotFoundException
		 * @throws Exception
		 */
		public static <T extends GenericEntityBean> Page<T> explodeList(Page<T> entityPage, Integer indexDeep) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, SecurityException, ClassNotFoundException, Exception {
				if ((entityPage != null) && (!entityPage.getContent().isEmpty())) {
						for (int i = 0; i < entityPage.getContent().size(); i++) {
								entityPage.getContent().get(i).explode(indexDeep);
						}
				}
				return entityPage;
		}

		/**
		 * Get all fields
		 *
		 * @param fields
		 * @param type
		 * @return
		 */
		public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
				fields.addAll(Arrays.asList(type.getDeclaredFields()));
				if (type.getSuperclass() != null) {
						fields = getAllFields(fields, type.getSuperclass());
				}
				return fields;
		}

		/**
		 * Check if exist dependencies into indicated set dependencies
		 *
		 * @param dependencies
		 * @param dependency
		 * @return
		 */
		public boolean existDependency(Set<String> dependencies, String dependency) {
				if (dependencies != null) {
						for (String select : dependencies) {
								if (select.equals(dependency)) {
										return true;
								}
						}
				}
				return false;
		}

		/**
		 * Get method for name
		 *
		 * @param methods
		 * @param name
		 * @return
		 */
		public Method searchMethod(Method[] methods, String name) {
				if ((methods != null) && (name != null)) {
						for (int i = 0; i < methods.length; i++) {
								String nameMethod = methods[i].getName();
								if (nameMethod.equals(name)) {
										return methods[i];
								}
						}
				}
				return null;
		}

		/**
		 * Returns all the interfaces that implements a specified class
		 *
		 * @param myClass
		 * @return superclass collection in string format
		 */
		public Set<String> getInterfaces(Class<?> myClass) {
				Set<String> dataSet = new HashSet<String>();
				Class<?>[] interfaces = myClass.getInterfaces();
				if (interfaces != null) {
						for (int i = 0; i < interfaces.length; i++) {
								if (!dataSet.contains(interfaces[i].toString())) {
										dataSet.addAll(getInterfaces(interfaces[i]));
								}
								if (interfaces[i].getSuperclass() != null) {
										dataSet.addAll(getInterfaces(interfaces[i].getSuperclass()));
								}
								dataSet.add(interfaces[i].getName());
								if (interfaces[i].getSuperclass() != null) {
										dataSet.add(interfaces[i].getSuperclass().getName());
								}
						}
				}
				return dataSet;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
				final int prime = 31;
				int result = super.hashCode();
				result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
				return result;
		}

		/**
		 * Returns all the interfaces that implements a specified class
		 *
		 * @param myClass
		 * @return interfaces collection in string format
		 */
		public Set<String> getSuperclasses(Class<?> myClass) {

				Set<String> dataSet = new HashSet<String>();
				if (myClass != null) {
						// add class
						if (!dataSet.contains(myClass.getName())) {
								dataSet.add(myClass.getName());
						}
						// called recursive method
						dataSet.addAll(getSuperclasses(myClass.getSuperclass()));
				}
				return dataSet;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
				if (this == obj) {
						return true;
				}
				GenericEntityBean other = (GenericEntityBean) obj;
				if (this.getId() == null) {
						if (other.getId() != null) {
								return false;
						}
				} else if (!this.getId().equals(other.getId())) {
						return false;
				}
				return true;
		}

		/**
		 * Get object parameters info
		 *
		 * @return string with class info
		 * @throws SecurityException
		 * @throws NoSuchMethodException
		 * @throws InvocationTargetException
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 */
		public String info() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
				Field[] fields = this.getClass().getDeclaredFields();
				String result = "";
				if (fields != null) {
						for (int i = 0; i < fields.length; i++) {
								if (isSimpleData(fields[i].getType())) {
										String nameMethod = "get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1);
										try {
												Method dtoGetMethod = this.getClass().getMethod(nameMethod);
												result += (result.trim().length() > 0) ? "; " : "";
												result += fields[i].getName() + "=" + dtoGetMethod.invoke(this);
										} catch (NoSuchMethodException e) {
												// DO NOTHING
										}
								}
						}
				}
				return this.getClass().getName() + " {" + result + "}";
		}

		/**
		 * Check if is a simple data
		 *
		 * @param object
		 * @return
		 */
		public boolean isSimpleData(Class<?> object) {
				if (object.isPrimitive()) {
						return true;
				}
				if ((object.getName().contains("Integer")) || (object.getName().contains("Float")) || (object.getName().contains("Double")) || (object.getName().contains("Date")) || (object.getName().contains("Boolean")) || (object.getName().contains("String"))) {
						return true;
				} else {
						return false;
				}
		}
}
