package org.lsmr.selfcheckout.control;

import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.CardReader;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.CardReaderListener;

/*
 * Listens to events from MembershipCard
 */
public class MembershipCardListener implements CardReaderListener {

private Checkout checkout;
	public MembershipCardListener(Checkout checkout) {
		this.checkout = checkout;
	}
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardTapped(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardSwiped(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardDataRead(CardReader reader, CardData data) {
		try {
			if(data.getType().equals("Membership")) {
				if(MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE.contains(data.getNumber())) {
					checkout.memberLogIn(data.getCardholder(), data.getNumber());
				}
			}
		}catch (CheckoutException e) {
			
		}
	}

	@Override
	public void cardInserted(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardRemoved(CardReader reader) {
		// TODO Auto-generated method stub
		
	}
}
