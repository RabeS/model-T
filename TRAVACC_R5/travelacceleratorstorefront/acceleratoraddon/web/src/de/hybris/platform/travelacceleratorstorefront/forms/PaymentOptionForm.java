/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.travelacceleratorstorefront.forms;

import java.util.List;


/**
 * The type Payment option form.
 */
public class PaymentOptionForm
{
	private List<PaymentTransactionForm> transactions;

	public List<PaymentTransactionForm> getTransactions()
	{
		return transactions;
	}

	public void setTransactions(final List<PaymentTransactionForm> transactions)
	{
		this.transactions = transactions;
	}


}
