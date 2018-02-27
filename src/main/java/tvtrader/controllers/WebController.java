package tvtrader.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import tvtrader.model.FormConfiguration;
import tvtrader.services.ConfigurationService;

@Controller
public class WebController extends WebMvcConfigurerAdapter {
	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
	    super.addResourceHandlers(registry);
	    registry.addResourceHandler("/css/**")
	        .addResourceLocations("classpath:/templates/css/");
	}
	
	@Autowired
	private ConfigurationService configService;
	
	@GetMapping(value = "/configuration")
	public ModelAndView showConfiguration() {
		ModelAndView model = new ModelAndView("configuration");
		
		FormConfiguration formConfiguration = configService.getFormConfiguration();
		model.addObject(formConfiguration);
		
		return model;
	}

	@PostMapping(value = "/configuration")
	public ModelAndView submit(@Valid FormConfiguration formConfiguration, BindingResult result) {

		if (result.hasErrors()) {
			return showConfiguration();
		}
		
		configService.cloneForm(formConfiguration);
		
		return showConfiguration();
	}

}
