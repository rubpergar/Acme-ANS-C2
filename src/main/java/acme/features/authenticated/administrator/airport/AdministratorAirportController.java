
package acme.features.authenticated.administrator.airport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.entities.airports.Airport;

@Controller
public class AdministratorAirportController extends AbstractGuiController<Administrator, Airport> {

	@Autowired
	private AdministratorAirportListService		listService;

	@Autowired
	private AdministratorAirportShowService		showService;

	@Autowired
	private AdministratorAirportCreateService	createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
	}

}
