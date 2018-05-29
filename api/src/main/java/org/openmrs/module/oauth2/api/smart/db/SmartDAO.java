/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.oauth2.api.smart.db;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Database methods for {@link SmartService}.
 */
@Component
public interface SmartDAO<T> {

	/*
	 * Add DAO methods here
	 */

	/**
	 * Save or Update details
	 *
	 * @param instance
	 */
	public T saveOrUpdate(T instance);

	/**
	 * @param id
	 * @return
	 */
	public T getById(Integer id);

	/**
	 * @return
	 */
	public List<T> getAll();

	/**
	 * @param instance
	 * @return
	 */
	public T update(T instance);

	/**
	 * @param instance
	 */
	public void delete(T instance);

	/**
	 * Use this method to merge detached object with existing entity before using {@link #saveOrUpdate(Object)}, {@link #update(Object)}
	 *
	 * @param instance
	 * @return
	 */
	public T merge(T instance);

}
