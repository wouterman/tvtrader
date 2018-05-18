package tvtrader.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;
import tvtrader.services.ConfigurationService;

import javax.validation.Valid;

@Controller
@EnableWebMvc
public class WebController extends WebMvcConfigurerAdapter {
	private ConfigurationService configService;

    @Autowired
    public WebController(ConfigurationService configService) {
        this.configService = configService;
    }

    @Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
	    super.addResourceHandlers(registry);
	    registry.addResourceHandler("/css/**")
	        .addResourceLocations("classpath:/css/");
	}

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/configuration").setViewName("configuration");
    }


	@GetMapping(value = "/configuration")
	public ModelAndView showConfiguration() {
		ModelAndView model = new ModelAndView("configuration");

		Configuration configuration = configService.getConfiguration();
		MailConfiguration mailConfiguration = configService.getMailConfiguration();

		model.addObject(configuration);
		model.addObject(mailConfiguration);

		return model;
	}

	@PostMapping(value = "/configuration")
	public ModelAndView submit(@Valid Configuration configuration, @Valid MailConfiguration mailConfiguration, BindingResult result) {

		if (result.hasErrors()) {
			return showConfiguration();
		}
		
		configService.cloneConfig(configuration);
		configService.cloneMailConfig(mailConfiguration);
		
		return showConfiguration();
	}

}
