/**
 * Inventory Management System - Client-side JavaScript
 * Handles: confirmations, flash message dismissal, sidebar toggle,
 *          form enhancements, and real-time quantity validation.
 */

document.addEventListener('DOMContentLoaded', function () {

  // ── Auto-dismiss flash messages after 5 seconds ───────────────────────
  document.querySelectorAll('.alert[data-auto-dismiss]').forEach(function (alert) {
    setTimeout(function () {
      alert.style.transition = 'opacity 0.5s';
      alert.style.opacity = '0';
      setTimeout(function () { alert.remove(); }, 500);
    }, 5000);
  });

  // ── Delete confirmation dialogs ───────────────────────────────────────
  document.querySelectorAll('[data-confirm]').forEach(function (el) {
    el.addEventListener('click', function (e) {
      var message = el.getAttribute('data-confirm');
      if (!confirm(message || 'Are you sure you want to delete this item? This action cannot be undone.')) {
        e.preventDefault();
        e.stopPropagation();
      }
    });
  });

  // ── Delete form submit confirmation ──────────────────────────────────
  document.querySelectorAll('form[data-confirm]').forEach(function (form) {
    form.addEventListener('submit', function (e) {
      var message = form.getAttribute('data-confirm');
      if (!confirm(message || 'Are you sure?')) {
        e.preventDefault();
      }
    });
  });

  // ── Sidebar mobile toggle ─────────────────────────────────────────────
  var toggleBtn = document.getElementById('sidebar-toggle');
  var sidebar = document.querySelector('.sidebar');
  if (toggleBtn && sidebar) {
    toggleBtn.addEventListener('click', function () {
      sidebar.classList.toggle('open');
    });
  }

  // ── SKU auto-uppercase ────────────────────────────────────────────────
  var skuInput = document.getElementById('sku');
  if (skuInput) {
    skuInput.addEventListener('input', function () {
      var pos = skuInput.selectionStart;
      skuInput.value = skuInput.value.toUpperCase();
      skuInput.setSelectionRange(pos, pos);
    });
  }

  // ── Stock quantity max validation against available stock ─────────────
  var qtyInput   = document.getElementById('quantity');
  var maxQty     = document.getElementById('maxAvailableQty');
  var qtyFeedback = document.getElementById('qtyFeedback');

  if (qtyInput && maxQty) {
    qtyInput.addEventListener('input', function () {
      var val = parseInt(qtyInput.value, 10);
      var max = parseInt(maxQty.value, 10);
      if (val > max) {
        qtyFeedback && (qtyFeedback.textContent = 'Exceeds available stock (' + max + ' units). Stock-out will be rejected.');
        qtyInput.classList.add('is-invalid');
      } else {
        qtyFeedback && (qtyFeedback.textContent = '');
        qtyInput.classList.remove('is-invalid');
      }
    });
  }

  // ── Product selector updates live stock display ───────────────────────
  var productSelect   = document.getElementById('productId');
  var stockDisplay    = document.getElementById('currentStockDisplay');

  if (productSelect && stockDisplay) {
    productSelect.addEventListener('change', function () {
      var selected = productSelect.options[productSelect.selectedIndex];
      var qty = selected.getAttribute('data-qty') || '-';
      var threshold = selected.getAttribute('data-threshold') || '-';
      stockDisplay.innerHTML =
        '<span class="fw-600">' + qty + '</span> units available' +
        ' &nbsp;|&nbsp; Min threshold: <span class="fw-600">' + threshold + '</span>';
      if (maxQty) maxQty.value = qty;
    });
  }

  // ── Price input formatting ────────────────────────────────────────────
  var priceInput = document.querySelector('input[name="price"]');
  if (priceInput) {
    priceInput.addEventListener('blur', function () {
      var val = parseFloat(priceInput.value);
      if (!isNaN(val)) {
        priceInput.value = val.toFixed(2);
      }
    });
  }

  // ── Table row clickable (goes to detail page) ─────────────────────────
  document.querySelectorAll('tr[data-href]').forEach(function (row) {
    row.style.cursor = 'pointer';
    row.addEventListener('click', function (e) {
      if (!e.target.closest('button, a, form')) {
        window.location.href = row.getAttribute('data-href');
      }
    });
  });
});
