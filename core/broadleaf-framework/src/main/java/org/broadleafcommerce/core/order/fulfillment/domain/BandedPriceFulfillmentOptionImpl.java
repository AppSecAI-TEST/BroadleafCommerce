/*
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2013 Broadleaf Commerce
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.broadleafcommerce.core.order.fulfillment.domain;

import org.broadleafcommerce.common.copy.CreateResponse;
import org.broadleafcommerce.common.copy.MultiTenantCopyContext;
import org.broadleafcommerce.common.extensibility.jpa.clone.IgnoreEnterpriseBehavior;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationCollection;
import org.broadleafcommerce.common.presentation.PopulateToOneFieldsEnum;
import org.broadleafcommerce.core.order.domain.FulfillmentOption;
import org.broadleafcommerce.core.order.domain.FulfillmentOptionImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Phillip Verheyden
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_FULFILLMENT_OPT_BANDED_PRC")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@AdminPresentationClass(friendlyName = "Banded Price Fulfillment Option",populateToOneFields = PopulateToOneFieldsEnum.TRUE)
public class BandedPriceFulfillmentOptionImpl extends FulfillmentOptionImpl implements BandedPriceFulfillmentOption {

    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy="option", targetEntity=FulfillmentPriceBandImpl.class)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @AdminPresentationCollection(friendlyName = "BandedPriceFulfillmentOptionBands", excluded = true)
    protected List<FulfillmentPriceBand> bands = new ArrayList<FulfillmentPriceBand>();

    @Override
    public List<FulfillmentPriceBand> getPriceBands() {
        return priceBands;
    }

    @Override
    public void setPriceBands(List<FulfillmentPriceBand> priceBands) {
        this.priceBands = priceBands;
    }

    @Override
    public CreateResponse<BandedPriceFulfillmentOption> createOrRetrieveCopyInstance(MultiTenantCopyContext context)
            throws CloneNotSupportedException {
        CreateResponse<BandedPriceFulfillmentOption> createResponse = super.createOrRetrieveCopyInstance(context);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        BandedPriceFulfillmentOption myClone = createResponse.getClone();

        for (FulfillmentPriceBand band : priceBands) {
            myClone.getPriceBands().add(band);
        }

        return createResponse;
    }

}
