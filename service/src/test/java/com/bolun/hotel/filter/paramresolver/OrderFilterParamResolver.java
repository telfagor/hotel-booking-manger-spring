package com.bolun.hotel.filter.paramresolver;

import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.util.OrderFilter;
import com.bolun.hotel.util.TestObjectsUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class OrderFilterParamResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == OrderFilter.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User user = TestObjectsUtils.getUser("tudor@gmail.com");
        user.setFirstName("Tudor");

        return OrderFilter.builder()
                .status(OrderStatus.REJECTED)
                .user(user)
                .build();
    }
}
