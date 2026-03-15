// JS to manage Actions dropdowns on the Today's Bake List (delegated events)
(function(){
  'use strict';

  const DROPDOWN_SELECTOR = '.cg-dropdown';
  const ACTION_BTN_SELECTOR = '.cg-action-btn';
  const DROPDOWN_ITEM_SELECTOR = '.cg-dropdown-item';

  let openDropdown = null;

  function closeOpenDropdown(){
    if(!openDropdown) return;
    const wrap = openDropdown.closest('.cg-action-wrap');
    const btn = wrap && wrap.querySelector(ACTION_BTN_SELECTOR);
    openDropdown.setAttribute('aria-hidden','true');
    if(btn) btn.setAttribute('aria-expanded','false');
    openDropdown.classList.remove('cg-dropdown--flip');
    openDropdown.style.display = 'none';
    openDropdown = null;
  }

  function measureAndFlip(dropdown){
    const prevDisplay = dropdown.style.display;
    const prevVisibility = dropdown.style.visibility;
    dropdown.style.visibility = 'hidden';
    dropdown.style.display = 'block';
    const rect = dropdown.getBoundingClientRect();
    const viewportWidth = window.innerWidth || document.documentElement.clientWidth;
    const shouldFlip = rect.right > viewportWidth - 8;
    dropdown.style.display = prevDisplay || 'none';
    dropdown.style.visibility = prevVisibility || '';
    return shouldFlip;
  }

  function setActiveItem(dropdown, selectedItem){
    const items = dropdown.querySelectorAll('.cg-dropdown-item');
    items.forEach(it => {
      it.classList.remove('active');
      it.removeAttribute('aria-current');
    });
    if(selectedItem){
      selectedItem.classList.add('active');
      selectedItem.setAttribute('aria-current','true');
    }
  }

  function openDropdownForButton(btn){
    const wrap = btn.closest('.cg-action-wrap');
    if(!wrap) return;
    const dropdown = wrap.querySelector(DROPDOWN_SELECTOR);
    if(!dropdown) return;

    if(openDropdown && openDropdown !== dropdown) closeOpenDropdown();

    const shouldFlip = measureAndFlip(dropdown);
    if(shouldFlip) dropdown.classList.add('cg-dropdown--flip'); else dropdown.classList.remove('cg-dropdown--flip');

    // highlight current status in dropdown
    try{
      const row = btn.closest('tr');
      const badge = row && row.querySelector('.cg-status-badge');
      if(badge){
        const currentStatus = badge.textContent.trim();
        const match = dropdown.querySelector(`.cg-dropdown-item[data-status="${currentStatus}"]`);
        setActiveItem(dropdown, match);
      }
    } catch(e){/* ignore */}

    dropdown.setAttribute('aria-hidden','false');
    dropdown.style.display = 'block';
    btn.setAttribute('aria-expanded','true');
    openDropdown = dropdown;
  }

  /**
   * Updates the order status badge in the UI.
   * Separated from persistence logic for cleaner code.
   */
  function updateStatusBadgeUI(row, status){
    const badge = row.querySelector('.cg-status-badge');
    if(!badge) return;

    badge.textContent = status;
    badge.classList.remove(
      'cg-badge--placed',
      'cg-badge--in-progress',
      'cg-badge--baked',
      'cg-badge--shipped',
      'cg-badge--delivered'
    );

    switch(status){
      case 'PLACED': badge.classList.add('cg-badge--placed'); break;
      case 'IN_PROGRESS': badge.classList.add('cg-badge--in-progress'); break;
      case 'BAKED': badge.classList.add('cg-badge--baked'); break;
      case 'SHIPPED': badge.classList.add('cg-badge--shipped'); break;
      case 'DELIVERED': badge.classList.add('cg-badge--delivered'); break;
    }
  }

  /**
   * Sends the status update to the backend via AJAX.
   * Updates UI on success, shows error on failure.
   */
  function persistStatusUpdate(orderId, newStatus, row, dropdown, selectedItem){
    // Get CSRF token from meta tag (if using Spring Security CSRF)
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    const formData = new URLSearchParams();
    formData.append('orderId', orderId);
    formData.append('newStatus', newStatus);

    const headers = {
      'Content-Type': 'application/x-www-form-urlencoded',
    };

    // Add CSRF header if present
    if(csrfToken && csrfHeader){
      headers[csrfHeader] = csrfToken;
    }

    fetch('/employee/order/update-status', {
      method: 'POST',
      headers: headers,
      body: formData
    })
    .then(response => {
      if(!response.ok){
        throw new Error('Failed to update order status');
      }
      return response.text();
    })
    .then(() => {
      // Success: update UI
      updateStatusBadgeUI(row, newStatus);
      if(dropdown) setActiveItem(dropdown, selectedItem);
      console.debug(`Order ${orderId} status updated to ${newStatus}`);
      
      // Optional: Show success feedback
      showToast('Status updated successfully', 'success');
    })
    .catch(error => {
      console.error('Error updating order status:', error);
      // Optional: Show error feedback
      showToast('Failed to update status. Please try again.', 'error');
    })
    .finally(() => {
      closeOpenDropdown();
    });
  }

  /**
   * Simple toast notification (optional - you can replace with your own notification system)
   */
  function showToast(message, type){
    // Simple console log for now - replace with your toast/notification system
    console.log(`[${type.toUpperCase()}] ${message}`);
    
    // Optional: Implement a simple toast UI
    // const toast = document.createElement('div');
    // toast.className = `toast toast-${type}`;
    // toast.textContent = message;
    // document.body.appendChild(toast);
    // setTimeout(() => toast.remove(), 3000);
  }

  function handleItemSelection(item){
    const status = item.getAttribute('data-status');
    const wrap = item.closest('.cg-action-wrap');
    const row = wrap && wrap.closest('tr');
    const dropdown = wrap && wrap.querySelector(DROPDOWN_SELECTOR);
    
    if(!row) return;

    // Get order ID from the action button's data attribute
    const btn = wrap && wrap.querySelector(ACTION_BTN_SELECTOR);
    const orderId = btn ? btn.getAttribute('data-order-id') : null;

    if(!orderId){
      console.error('Order ID not found on action button');
      closeOpenDropdown();
      return;
    }

    // Send update to backend
    persistStatusUpdate(orderId, status, row, dropdown, item);
  }

  function delegatedClickHandler(e){
    const btn = e.target.closest(ACTION_BTN_SELECTOR);
    if(btn){
      e.preventDefault();
      // toggle
      const wrap = btn.closest('.cg-action-wrap');
      const dropdown = wrap && wrap.querySelector(DROPDOWN_SELECTOR);
      if(!dropdown) return;
      if(openDropdown === dropdown){ closeOpenDropdown(); } else { openDropdownForButton(btn); }
      return;
    }

    const item = e.target.closest(DROPDOWN_ITEM_SELECTOR);
    if(item){
      e.preventDefault();
      handleItemSelection(item);
      return;
    }

    // click elsewhere closes any open dropdown
    if(openDropdown && !openDropdown.contains(e.target)){
      closeOpenDropdown();
    }
  }

  function bind(){
    // Use delegated click handling to be resilient
    document.addEventListener('click', delegatedClickHandler);
    // ESC to close
    document.addEventListener('keydown', function(e){ if(e.key === 'Escape') closeOpenDropdown(); });
    console.debug('employee-dashboard: bound delegated click handler for actions dropdown');
  }

  // Wait for DOM
  if(document.readyState === 'loading'){
    document.addEventListener('DOMContentLoaded', bind);
  } else {
    bind();
  }
})();