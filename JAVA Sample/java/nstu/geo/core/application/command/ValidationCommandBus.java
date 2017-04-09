package nstu.geo.core.application.command;

import nstu.geo.core.infrastructure.exeption.ValidationException;


public final class ValidationCommandBus implements CommandBus  {

    private CommandBus next;

    private Validator validator;

    public ValidationCommandBus(final CommandBus next) {
        this.next = next;
    }

    public void execute(final Command command)
    {
        this.resolveHValidator(command);
        try {
            this.validator.validate(command);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        this.next.execute(command);
    }

    private void resolveHValidator(final Command command)
    {
        String commandClassName = command.getClass().getName();
        String validatorClassName = commandClassName.substring(0, commandClassName.length() - 7) +  "Validator";
        try {
            this.validator = (Validator) Class.forName(validatorClassName).newInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
