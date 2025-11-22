(() => {
    const contextPath = document.body.dataset.contextPath || "";
    const productsEndpoint = `${contextPath}/products`;

    const tableBody = document.querySelector("#products-table tbody");
    const statusLabel = document.getElementById("status-message");
    const countLabel = document.getElementById("product-count");
    const toast = document.getElementById("toast");

    const createBtn = document.getElementById("create-btn");
    const searchBtn = document.getElementById("search-btn");
    const resetBtn = document.getElementById("reset-btn");
    const searchInput = document.getElementById("search-input");

    const modal = document.getElementById("product-modal");
    const modalMode = document.getElementById("modal-mode");
    const modalTitle = document.getElementById("modal-title");
    const closeModalBtn = document.getElementById("close-modal");
    const cancelBtn = document.getElementById("cancel-btn");
    const form = document.getElementById("product-form");
    const errorBox = document.getElementById("form-error");
    const submitBtn = document.getElementById("submit-btn");

    const defaultQuery = new URLSearchParams({ size: 50, sort: "id,desc" });
    let currentItems = [];
    let editingId = null;

    const formatCurrency = (value) =>
        new Intl.NumberFormat("es-AR", {
            style: "currency",
            currency: "ARS",
            minimumFractionDigits: 2,
        }).format(Number(value));

    const showToast = (message, isError = false) => {
        toast.textContent = message;
        toast.style.backgroundColor = isError ? "#b91c1c" : "#1d4ed8";
        toast.classList.add("show");
        setTimeout(() => toast.classList.remove("show"), 2600);
    };

    const setStatus = (message) => {
        statusLabel.textContent = message;
    };

    const request = async (url, options = {}) => {
        const response = await fetch(url, {
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
            ...options,
        });

        if (!response.ok) {
            let message = response.statusText || "Error inesperado";
            const body = await response.text();
            if (body) {
                try {
                    const payload = JSON.parse(body);
                    message = payload.message || payload.error || message;
                } catch {
                    message = body;
                }
            }
            throw new Error(message);
        }

        if (response.status === 204) {
            return null;
        }

        return response.json();
    };

    const renderEmpty = (message) => {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="empty-row">${message}</td>
            </tr>
        `;
    };

    const renderTable = (items) => {
        currentItems = items;
        countLabel.textContent = `${items.length} ${
            items.length === 1 ? "producto" : "productos"
        }`;

        if (!items.length) {
            renderEmpty("Sin productos cargados");
            return;
        }

        tableBody.innerHTML = items
            .map(
                (item) => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.nombre}</td>
                    <td>${item.descripcion}</td>
                    <td>${formatCurrency(item.precio)}</td>
                    <td>
                        <div class="actions">
                            <button class="action-btn edit" data-action="edit" data-id="${item.id}">Editar</button>
                            <button class="action-btn delete" data-action="delete" data-id="${item.id}">Eliminar</button>
                        </div>
                    </td>
                </tr>
            `
            )
            .join("");
    };

    const loadAllProducts = async () => {
        setStatus("Cargando productos…");
        try {
            const data = await request(`${productsEndpoint}?${defaultQuery.toString()}`);
            renderTable(data.content || []);
            setStatus("Productos sincronizados correctamente.");
        } catch (error) {
            renderEmpty("Ocurrió un error al cargar los productos");
            setStatus(error.message);
            showToast(error.message, true);
        }
    };

    const searchProducts = async (query) => {
        if (!query) {
            await loadAllProducts();
            return;
        }

        setStatus(`Buscando "${query}"…`);
        try {
            const data = await request(`${productsEndpoint}/name/${encodeURIComponent(query)}`);
            renderTable(data);
            setStatus(data.length ? "Resultados encontrados." : "Sin coincidencias.");
        } catch (error) {
            renderEmpty("Sin resultados");
            setStatus(error.message);
            showToast(error.message, true);
        }
    };

    const openModal = (mode, product) => {
        editingId = product?.id ?? null;
        modalMode.textContent = mode === "edit" ? "Editar producto" : "Nuevo producto";
        modalTitle.textContent = mode === "edit" ? product.nombre : "Crear producto";
        form.nombre.value = product?.nombre ?? "";
        form.descripcion.value = product?.descripcion ?? "";
        form.precio.value = product?.precio ?? "";
        errorBox.textContent = "";
        submitBtn.textContent = mode === "edit" ? "Guardar cambios" : "Crear producto";
        modal.classList.add("show");
        modal.setAttribute("aria-hidden", "false");
    };

    const closeModal = () => {
        modal.classList.remove("show");
        modal.setAttribute("aria-hidden", "true");
        form.reset();
        editingId = null;
        errorBox.textContent = "";
    };

    const handleEdit = async (id) => {
        try {
            const product = await request(`${productsEndpoint}/${id}`);
            openModal("edit", product);
        } catch (error) {
            showToast(error.message, true);
        }
    };

    const handleDelete = async (id) => {
        if (!confirm("¿Eliminar este producto? Esta acción no se puede deshacer.")) {
            return;
        }

        try {
            await request(`${productsEndpoint}/${id}`, { method: "DELETE" });
            showToast("Producto eliminado");
            await loadAllProducts();
        } catch (error) {
            showToast(error.message, true);
        }
    };

    const persistProduct = async (payload) => {
        const options = {
            method: editingId ? "PUT" : "POST",
            body: JSON.stringify(payload),
        };
        const url = editingId
            ? `${productsEndpoint}/${editingId}`
            : productsEndpoint;
        return request(url, options);
    };

    // Event listeners
    tableBody.addEventListener("click", (event) => {
        const button = event.target.closest("button[data-action]");
        if (!button) return;
        const id = button.dataset.id;
        if (button.dataset.action === "edit") {
            handleEdit(id);
        } else if (button.dataset.action === "delete") {
            handleDelete(id);
        }
    });

    createBtn.addEventListener("click", () => openModal("create"));
    closeModalBtn.addEventListener("click", closeModal);
    cancelBtn.addEventListener("click", closeModal);
    modal.addEventListener("click", (event) => {
        if (event.target === modal) {
            closeModal();
        }
    });

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const payload = {
            nombre: form.nombre.value.trim(),
            descripcion: form.descripcion.value.trim(),
            precio: form.precio.value ? Number(form.precio.value) : null,
        };

        if (!payload.nombre || !payload.descripcion || !payload.precio) {
            errorBox.textContent = "Todos los campos son obligatorios.";
            return;
        }

        submitBtn.disabled = true;
        submitBtn.textContent = editingId ? "Guardando…" : "Creando…";

        try {
            await persistProduct(payload);
            showToast(editingId ? "Producto actualizado" : "Producto creado");
            closeModal();
            await loadAllProducts();
        } catch (error) {
            errorBox.textContent = error.message;
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = editingId ? "Guardar cambios" : "Crear producto";
        }
    });

    searchBtn.addEventListener("click", () => searchProducts(searchInput.value.trim()));
    searchInput.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
            searchProducts(searchInput.value.trim());
        }
    });
    resetBtn.addEventListener("click", () => {
        searchInput.value = "";
        loadAllProducts();
    });

    // Bootstrap
    loadAllProducts();
})();
