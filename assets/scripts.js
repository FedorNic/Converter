async function submit() {
  setLoading(true);
  const url = document.getElementById("img-url").value;
  if (!url) {
    error("The URL cannot be empty!")
    return;
  }
  try {
    const resp = await fetch('/convert', {
      method: "POST",
      body: url
    });
    if (!resp.ok) {
      error("The conversion failed &#128549;")
      return;
    }
    const txtImgRaw = await resp.text();
    console.log(txtImgRaw.split("\n").length);
    const txtImgCleaned = txtImgRaw.replaceAll("\n", "<br/>");
    console.log(txtImgCleaned.split("<br/>").length);
    console.log(txtImgCleaned)
    document.getElementById("txtimg").innerHTML = txtImgCleaned;
    success("Your image has been converted!");
  } catch (e) {
    error("The conversion failed &#128557;")
  }
}

function clearStatus() {
  document.getElementById("alert").innerHTML = "";
}

function status(msg, type) {
  document.getElementById("alert").innerHTML = `
    <div class="alert alert-${type}" role="alert">
      ${msg}
    </div>
  `;
}

function success(msg) {
  status(msg, "success");
}

function error(msg) {
  status(msg, "danger");
}

// Долго грузится...
function info(msg) {
  status(msg, "info");
}

function setLoading(isLoading) {
  if (isLoading) {
    const spinner = `
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Загрузка...</span>
      </div>`;
    status(spinner, "info");
  } else {
    clearStatus();
  }
}