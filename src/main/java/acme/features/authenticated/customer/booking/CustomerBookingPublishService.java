
package acme.features.authenticated.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flights.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Booking booking;
		int bookingId;
		int userAccountId;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(booking.getCustomer().getUserAccount().getId() == userAccountId);

		if (!booking.getIsDraft())
			super.state(booking.getIsDraft(), "*", "customer.booking.form.error.notDraft", "isDraft");
	}

	@Override
	public void load() {
		Booking booking;
		int bookingId;

		bookingId = this.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		assert booking != null;
		super.bindObject(booking, "locatorCode", "flight", "purchaseMoment", "travelClass", "lastNibble", "isDraft");
	}

	@Override
	public void validate(final Booking booking) {
		assert booking != null;

		//Tengo que comprobar the last credit card nibble has been stored. 
		String lastNibble = this.repository.findLastNibbleById(booking.getId());
		super.state(!lastNibble.isEmpty(), "*", "customer.project.publish.error.lastNibbleNotPublished");
	}

	@Override
	public void perform(final Booking booking) {
		assert booking != null;
		Collection<Passenger> passengers;

		booking.setIsDraft(false);
		// Cuando se publica el booking, los pasajeros también se publican
		passengers = this.repository.findAllPassengersByBookingId(booking.getId());
		for (Passenger p : passengers) {
			p.setIsDraft(false);
			this.repository.save(p);
		}
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		List<Flight> nonDraftFlights = this.repository.findNotDraftFlights().stream().toList();
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		SelectChoices flights = SelectChoices.from(nonDraftFlights, "id", booking.getFlight());
		List<Passenger> passengers = this.repository.findAllPassengersByBookingId(booking.getId()).stream().toList();
		Dataset dataset;
		dataset = super.unbindObject(booking, "locatorCode", "flight", "purchaseMoment", "travelClass", "price", "lastNibble", "isDraft");
		dataset.put("travelClass", travelClasses);
		dataset.put("flight", flights);
		dataset.put("passenger", !passengers.isEmpty());
		super.getResponse().addData(dataset);
	}

}
