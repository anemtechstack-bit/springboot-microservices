function confirmAction(msg){
  return confirm(msg || "Are you sure?");
}

function initReturnForm(){
  const orderSel = document.getElementById("orderSelect");
  const productSel = document.getElementById("productSelect");
  const qtyInput = document.getElementById("qtyInput");
  const orderDataEl = document.getElementById("ordersJson");
  if(!orderSel || !productSel || !orderDataEl) return;

  const orders = JSON.parse(orderDataEl.textContent || "[]");

  function populateProducts(){
    const orderId = Number(orderSel.value);
    const order = orders.find(o => o.id === orderId);
    productSel.innerHTML = "";
    if(!order){ return; }
    order.items.forEach(it => {
      const opt = document.createElement("option");
      opt.value = it.productId;
      opt.textContent = `${it.productName} (ordered: ${it.qty})`;
      opt.dataset.max = it.qty;
      productSel.appendChild(opt);
    });
    if(productSel.options.length > 0){
      qtyInput.max = productSel.options[0].dataset.max || 1;
      qtyInput.value = 1;
    }
  }

  function syncQtyMax(){
    const sel = productSel.options[productSel.selectedIndex];
    if(!sel) return;
    qtyInput.max = sel.dataset.max || 1;
    if(Number(qtyInput.value) > Number(qtyInput.max)) qtyInput.value = qtyInput.max;
  }

  orderSel.addEventListener("change", populateProducts);
  productSel.addEventListener("change", syncQtyMax);

  populateProducts();
}

document.addEventListener("DOMContentLoaded", initReturnForm);
