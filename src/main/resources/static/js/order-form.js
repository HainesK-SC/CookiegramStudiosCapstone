// This script handles the logic for item quantity on the /order page
// Makes it possible for the arrows to be used to increment and decrement
// the qty for a given item

document.addEventListener('DOMContentLoaded', function() {
	const cards = document.querySelectorAll('.order-item-card');
	cards.forEach(function(card) {
		const decrementBtn = card.querySelector('.qty-btn:first-of-type');
		const incrementBtn = card.querySelector('.qty-btn:last-of-type');
		const qtyDisplay = card.querySelector('.order-qty-number');
		const qtyInput = card.querySelector('input[name="productQuantity"]');
	
		incrementBtn.addEventListener('click', function() {
				let currentQty = parseInt(qtyDisplay.textContent);
				currentQty++;
				qtyDisplay.textContent = currentQty;
				qtyInput.value = currentQty;
		});
			
		decrementBtn.addEventListener('click', function() {
				let currentQty = parseInt(qtyDisplay.textContent);
				if(currentQty >= 1) {
					currentQty--;
					qtyDisplay.textContent = currentQty;
					qtyInput.value = currentQty;	
				}
		});
		
	});
});