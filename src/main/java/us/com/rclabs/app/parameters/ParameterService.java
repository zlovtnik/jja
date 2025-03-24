package us.com.rclabs.app.parameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParameterService {

    @Autowired
    private ParameterRepository parameterRepository;

    public Parameter createParameter(Parameter parameter) {
        return parameterRepository.save(parameter);
    }

    public Optional<Parameter> getParameterById(Long id) {
        return parameterRepository.findById(id);
    }

    public List<Parameter> getAllParameters() {
        return parameterRepository.findAll();
    }

    public Parameter updateParameter(Long id, Parameter parameterDetails) {
        Parameter parameter = parameterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parameter not found with id " + id));

        parameter.setName(parameterDetails.getName());
        parameter.setValue(parameterDetails.getValue());

        return parameterRepository.save(parameter);
    }

    public void deleteParameter(Long id) {
        Parameter parameter = parameterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parameter not found with id " + id));

        parameterRepository.delete(parameter);
    }
}
