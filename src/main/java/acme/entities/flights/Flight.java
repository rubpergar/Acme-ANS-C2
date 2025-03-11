
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.entities.legs.LegRepository;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlight
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				indication;

	@Mandatory
	@ValidMoney(min = 0, max = 1000000)
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager				airlineManager;

	// Atributos derivados ---------------------------------


	@Transient
	public Date getScheduledDeparture() {
		Date scheduledDep;
		LegRepository repo;

		repo = SpringHelper.getBean(LegRepository.class);
		scheduledDep = repo.getFirstLegByFlight(this.getId()).getScheduledDeparture();
		return scheduledDep;
	}

	@Transient
	public Date getScheduledArrival() {
		Date scheduledAr;
		LegRepository repo;

		repo = SpringHelper.getBean(LegRepository.class);
		scheduledAr = repo.getLastLegByFlight(this.getId()).getScheduledArrival();
		return scheduledAr;
	}

	@Transient
	public String getOriginCity() {
		String origCity;
		LegRepository repo;

		repo = SpringHelper.getBean(LegRepository.class);
		origCity = repo.getFirstLegByFlight(this.getId()).getDepartureAirport().getCity();
		return origCity;
	}

	@Transient
	public String getDestinationCity() {
		String destCity;
		LegRepository repo;

		repo = SpringHelper.getBean(LegRepository.class);
		destCity = repo.getLastLegByFlight(this.getId()).getArrivalAirport().getCity();
		return destCity;
	}

	@Transient
	public int getNumberOfLayovers() {
		Integer numbLay;
		LegRepository repo;

		repo = SpringHelper.getBean(LegRepository.class);
		numbLay = repo.getNumberLegsByFlight(this.getId());
		return numbLay - 1;
	}

}
