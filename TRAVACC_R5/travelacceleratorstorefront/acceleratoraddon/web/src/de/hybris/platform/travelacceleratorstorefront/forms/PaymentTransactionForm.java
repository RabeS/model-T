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
 * The type Payment transaction form.
 */
public class PaymentTransactionForm
{
	private List<String> entryNumbers;
	private String amount;

	public List<String> getEntryNumbers()
	{
		return entryNumbers;
	}

	public void setEntryNumbers(final List<String> entryNumbers)
	{
		this.entryNumbers = entryNumbers;
	}

	public String getAmount()
	{
		return amount;
	}

	public void setAmount(final String amount)
	{
		this.amount = amount;
	}


}
