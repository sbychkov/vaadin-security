package org.ilay;

import java.lang.ref.Reference;
import java.util.Objects;

/**
 * reverts all operations on 'data' ( instead of 'objects' ), like Authorization.restrictData()
 *
 * @author Bernd Hopp bernd@vaadin.com
 */
class DataReverter extends ReverterBase {

    private final Reference<VaadinAbstraction.DataProviderHolder> hasDataProvider;

    DataReverter(Reference<VaadinAbstraction.DataProviderHolder> hasDataProvider) {
        this.hasDataProvider = Objects.requireNonNull(hasDataProvider);
    }

    @Override
    void revertInternal() {

        final VaadinAbstraction.DataProviderHolder dataProviderHolder = this.hasDataProvider.get();

        if (dataProviderHolder != null) {
            AuthorizationContext.getCurrent().unbindData(dataProviderHolder);
        }
    }
}
