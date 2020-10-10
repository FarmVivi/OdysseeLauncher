package fr.odyssee.bootstrap.parameters;

import java.util.HashMap;
import java.util.Map;

public class ParametersManager {
    private static final Map<Parameters, String> parameters = new HashMap<>();

    public static void addParameter(Parameters parameter, String value) {
        parameters.put(parameter, value);
    }

    public static String getParameter(Parameters parameter) throws UnableToGetParameterException {
        if (parameters.isEmpty() || !parameters.containsKey(parameter)) {
            throw new UnableToGetParameterException(parameter);
        } else {
            return parameters.get(parameter);
        }
    }
}
