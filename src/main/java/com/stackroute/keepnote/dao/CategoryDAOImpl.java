package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */

@Repository
@Transactional
public class CategoryDAOImpl implements CategoryDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public CategoryDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory=sessionFactory;
	}

	/*
	 * Create a new category
	 */
	public boolean createCategory(Category category) {
		sessionFactory.getCurrentSession().save(category);
		sessionFactory.getCurrentSession().flush();
		return true;
	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) {
		boolean flag = true;
		
		try {
			if(getCategoryById(categoryId)==null)
				flag =false;
			else {
				sessionFactory.getCurrentSession().delete(getCategoryById(categoryId));
				sessionFactory.getCurrentSession().flush();
			}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (CategoryNotFoundException e) {
			e.printStackTrace();
		}
		return flag;

	}
	/*
	 * Update an existing category
	 */

	public boolean updateCategory(Category category) {
		boolean flag = true;
		
		try {
			if(getCategoryById(category.getCategoryId())==null)
				flag =false;
			else {
				sessionFactory.getCurrentSession().clear();
				sessionFactory.getCurrentSession().update(category);
				sessionFactory.getCurrentSession().flush();
			}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (CategoryNotFoundException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/*
	 * Retrieve details of a specific category
	 */

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {
		Category category= sessionFactory.getCurrentSession().get(Category.class,categoryId);
		
		if(category==null) {
			throw new CategoryNotFoundException("CategoryNotFoundException");
		}
		else {
			sessionFactory.getCurrentSession().flush();
			return category;
		}
	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		String hql= "FROM Category category where categoryCreatedBy = :userId";
		List<Category> result = sessionFactory.getCurrentSession().createQuery(hql).setParameter("userId", userId).getResultList();
		return result;
	}

}
