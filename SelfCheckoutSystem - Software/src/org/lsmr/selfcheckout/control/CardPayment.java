package org.lsmr.selfcheckout.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;

import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.external.CardIssuer;

public class CardPayment {

	public enum PaymentType {
		DEBIT("debit"), CREDIT("credit");

		private String name;

		PaymentType(String type) {
			this.name = type;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	public enum CardError {
		WRONG_PIN, SWIPE_ERROR, INVALID_CARD_TYPE, CHIP_FAILURE, AUTHORIZATION_FAIL, TAP_FAIL
	}

	private boolean ready;
	private SelfCheckoutStation checkoutStation;
	private PaymentType type = null;
	private CardIssuer cardIssuer;
	private BigDecimal amount;

	public CardPayment(SelfCheckoutStation station) {
		checkoutStation = station;
		ready = false;
	}

	/**
	 * Prepares the machine to accept card payments
	 * 
	 * @param type
	 *            the payment method, either Credit or Debit
	 * @param cardIssuer
	 *            the company that issued the card
	 * @param amt
	 *            the balance to pay
	 * @throws CheckoutException
	 */
	public void initialize(PaymentType type, CardIssuer cardIssuer, BigDecimal amt) throws CheckoutException {

		this.amount = amt;
		this.cardIssuer = cardIssuer;

		ready = true;
		checkoutStation.cardReader.enable();

		this.type = type;
	}

	/**
	 * Reset the state of the card payment
	 */
	public void reset() {
		ready = false;
	}

	/**
	 * Swipes a card on the card reader
	 * 
	 * @param card
	 *            the card to swipe
	 * @param signature
	 *            the customer's signature
	 * @return null if payment succeeded, otherwise a Pair instance of an error type
	 *         is returned.
	 */
	public Pair<CardError, String> swipe(Card card, BufferedImage signature) {
		try {
			CardData data = checkoutStation.cardReader.swipe(card, signature);

			// user specified card is debit/credit but received credit/debit
			if (!data.getType().toLowerCase().equals(type.name)) {
				return new Pair<>(CardError.INVALID_CARD_TYPE, data.getType());
			}

			int holdNum = cardIssuer.authorizeHold(data.getNumber(), amount);
			if (holdNum == -1) {
				return new Pair<>(CardError.AUTHORIZATION_FAIL);
			}
		} catch (IOException e) {
			return new Pair<>(CardError.SWIPE_ERROR);
		}

		reset();
		return null;
	}

	/**
	 * Inserts a card on the card reader
	 * 
	 * @param card
	 *            the card to insert
	 * @param pin
	 *            the pin of the card
	 * @return null if payment succeeded, otherwise a Pair instance of an error type
	 *         is returned.
	 */
	public Pair<CardError, String> insert(Card card, String pin) {
		try {
			CardData data = checkoutStation.cardReader.insert(card, pin);

			// user specified card is debit/credit but received credit/debit
			if (!data.getType().toLowerCase().equals(type.name)) {
				return new Pair<>(CardError.INVALID_CARD_TYPE);
			}

			int holdNum = cardIssuer.authorizeHold(data.getNumber(), amount);
			if (holdNum == -1) {
				return new Pair<>(CardError.AUTHORIZATION_FAIL);
			}

		} catch (IOException e) {
			return new Pair<>(CardError.CHIP_FAILURE);
		}

		// success
		reset();
		return null;
	}

	/**
	 * Taps the card on the card reader
	 * 
	 * @param card
	 *            the card to tap
	 * @return null if payment succeeded, otherwise a Pair instance of an error type
	 *         is returned.
	 */
	public Pair<CardError, String> tap(Card card) {
		// verify that the machine can read the card
		try {
			CardData data = checkoutStation.cardReader.tap(card);
			if (data != null) {
				// user specified card is debit/credit but received credit/debit
				if (!data.getType().toLowerCase().equals(type.name)) {
					return new Pair<>(CardError.INVALID_CARD_TYPE, data.getType());
				}

				int holdNum = cardIssuer.authorizeHold(data.getNumber(), amount);
				if (holdNum == -1) {
					return new Pair<>(CardError.AUTHORIZATION_FAIL);
				}
			} else {
				return new Pair<>(CardError.TAP_FAIL);
			}
		} catch (IOException e) {
			return new Pair<>(CardError.CHIP_FAILURE);
		}

		// success

		return null;
	}

	/**
	 * Returns the card type the payment machine is expecting
	 * 
	 * @return the expected card type
	 */
	public PaymentType getPaymentType() {
		return type;
	}

	/**
	 * Checks if the machine is ready to accept card payments
	 * 
	 * @return true if the machine is initialized, false otherwise
	 */
	public boolean isReady() {
		return ready;
	}
}
