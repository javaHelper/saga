package com.example.command.interceptors;

import com.example.command.CreateProductCommand;
import com.example.core.data.ProductLookupEntity;
import com.example.core.data.ProductLookupRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;


/**
 * This class also does the validation if you don't have an option to do bean level validation..
 */

@Slf4j
@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    @Autowired
    private ProductLookupRepository productLookupRepository;

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            log.info("### Intercepted command " + command.getPayloadType());
            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

//                if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0)
//                    throw new IllegalArgumentException("Price can not be less than or equals to zero");
//
//                if (StringUtils.isEmpty(createProductCommand.getTitle()))
//                    throw new IllegalArgumentException("Title can not be empty!");

                ProductLookupEntity entity = productLookupRepository.findByProductIdOrTitle(
                        createProductCommand.getProductId(), createProductCommand.getTitle());
                if (entity != null) {
                    throw new IllegalArgumentException(
                            String.format("### Product with ProductId %s or title %s already exist", entity.getProductId(), entity.getTitle()));
                }

            }
            return command;
        };
    }
}
