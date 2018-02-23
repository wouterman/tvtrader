package tvtrader.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import tvtrader.model.FormConfiguration;

@Controller
public class WebController extends WebMvcConfigurerAdapter {
	
	private FormConfiguration formConfiguration;
	
	@ModelAttribute("formConfiguration")
	public FormConfiguration getFormConfiguration() {
		return formConfiguration;
	}
	
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/configuration").setViewName("configuration");
	}

	@GetMapping(value = "/configuration")
	public String showConfiguration() {
		return "configuration";
	}

	@PostMapping(value = "/configuration")
	public String submit(@Valid FormConfiguration formConfiguration, BindingResult result) {

		if (result.hasErrors()) {
			return "configuration";
		}

		this.formConfiguration = formConfiguration;
		
		return showConfiguration();
	}

}
