using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.Globalization;
using System.Security;
using System.Text;
using System.Xml.Linq;

namespace ClientePagos.Pages
{
    public class IndexModel : PageModel
    {
        [BindProperty]
        public string CI { get; set; } = "";

        [BindProperty]
        public string Nombres { get; set; } = "";

        [BindProperty]
        public string PrimerApellido { get; set; } = "";

        [BindProperty]
        public string SegundoApellido { get; set; } = "";

        public string Mensaje { get; set; } = "";

        public List<DeudaVista> Deudas { get; set; } = new();

        private const string SOAP_URL =
            "http://localhost:5000/ServicioPagos.svc";

        public void OnGet()
        {
        }

        // BOTON CONSULTAR DEUDAS
        public async Task OnPostConsultarAsync()
        {
            await ConsultarDeudasAsync();
        }

        // BOTON PAGAR
        public async Task OnPostPagarAsync(int id, string empresa)
        {
            try
            {
                string soap = $"""
                <?xml version="1.0" encoding="utf-8"?>
                <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
                  <s:Body>
                    <PagarDeuda xmlns="http://tempuri.org/">
                      <c xmlns:a="http://schemas.datacontract.org/2004/07/ServidorPagos.Models">
                        <a:Anio>0</a:Anio>
                        <a:CI>{Escapar(CI)}</a:CI>
                        <a:Empresa>{Escapar(empresa)}</a:Empresa>
                        <a:Estado>emitida</a:Estado>
                        <a:Id>{id}</a:Id>
                        <a:Mes></a:Mes>
                        <a:NombreCompleto></a:NombreCompleto>
                        <a:NumeroFactura>0</a:NumeroFactura>
                        <a:TotalFactura>0</a:TotalFactura>
                      </c>
                    </PagarDeuda>
                  </s:Body>
                </s:Envelope>
                """;

                string xml = await EnviarSoapAsync(
                    "http://tempuri.org/IServicioPagos/PagarDeuda",
                    soap
                );

                XDocument documento = XDocument.Parse(xml);

                string? error = BuscarFault(documento);

                if (error != null)
                {
                    Mensaje = error;
                    return;
                }

                string resultado = documento
                    .Descendants()
                    .FirstOrDefault(x =>
                        x.Name.LocalName == "PagarDeudaResult")
                    ?.Value ?? "false";

                if (resultado.Equals(
                    "true",
                    StringComparison.OrdinalIgnoreCase))
                {
                    await ConsultarDeudasAsync();

                    Mensaje =
                        "Pago realizado correctamente. " +
                        $"Quedan {Deudas.Count} deudas pendientes.";
                }
                else
                {
                    Mensaje =
                        "No se pudo realizar el pago de la deuda.";
                }
            }
            catch (Exception ex)
            {
                Mensaje =
                    "Error al realizar el pago: " + ex.Message;
            }
        }

        private async Task ConsultarDeudasAsync()
        {
            Deudas.Clear();

            try
            {
                string soap = $"""
                <?xml version="1.0" encoding="utf-8"?>
                <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
                  <s:Body>
                    <verDeuda xmlns="http://tempuri.org/">
                      <CI>{Escapar(CI)}</CI>
                      <primerApellido>{Escapar(PrimerApellido)}</primerApellido>
                      <segundoApellido>{Escapar(SegundoApellido)}</segundoApellido>
                      <nombres>{Escapar(Nombres)}</nombres>
                    </verDeuda>
                  </s:Body>
                </s:Envelope>
                """;

                string xml = await EnviarSoapAsync(
                    "http://tempuri.org/IServicioPagos/verDeuda",
                    soap
                );

                XDocument documento = XDocument.Parse(xml);

                string? error = BuscarFault(documento);

                if (error != null)
                {
                    Mensaje = error;
                    return;
                }

                var elementosDeuda = documento
                    .Descendants()
                    .Where(x =>
                        x.Name.LocalName == "Deuda");

                foreach (XElement deuda in elementosDeuda)
                {
                    Deudas.Add(new DeudaVista
                    {
                        Id = LeerInt(deuda, "Id"),
                        Empresa = Leer(deuda, "Empresa"),
                        CI = Leer(deuda, "CI"),
                        NombreCompleto =
                            Leer(deuda, "NombreCompleto"),

                        NumeroFactura =
                            LeerInt(deuda, "NumeroFactura"),

                        Anio = LeerInt(deuda, "Anio"),
                        Mes = Leer(deuda, "Mes"),

                        TotalFactura =
                            LeerDecimal(
                                deuda,
                                "TotalFactura"
                            ),

                        Estado = Leer(deuda, "Estado")
                    });
                }

                if (Deudas.Count == 0)
                {
                    Mensaje =
                        "No se encontraron deudas pendientes.";
                }
                else
                {
                    Mensaje =
                        $"Se encontraron {Deudas.Count} deudas pendientes.";
                }
            }
            catch (Exception ex)
            {
                Mensaje =
                    "No se pudo consultar el servidor de pagos: " +
                    ex.Message;
            }
        }

        private async Task<string> EnviarSoapAsync(
            string soapAction,
            string soap)
        {
            using HttpClient cliente = new HttpClient();

            using HttpRequestMessage request =
                new HttpRequestMessage(
                    HttpMethod.Post,
                    SOAP_URL
                );

            request.Content = new StringContent(
                soap,
                Encoding.UTF8,
                "text/xml"
            );

            request.Headers.TryAddWithoutValidation(
                "SOAPAction",
                $"\"{soapAction}\""
            );

            HttpResponseMessage response =
                await cliente.SendAsync(request);

            return await response.Content.ReadAsStringAsync();
        }

        private static string? BuscarFault(
            XDocument documento)
        {
            XElement? fault = documento
                .Descendants()
                .FirstOrDefault(x =>
                    x.Name.LocalName == "Fault");

            if (fault == null)
                return null;

            return fault
                .Descendants()
                .FirstOrDefault(x =>
                    x.Name.LocalName == "faultstring")
                ?.Value
                ?? "El servidor SOAP devolvio un error.";
        }

        private static string Escapar(string? valor)
        {
            return SecurityElement.Escape(valor ?? "") ?? "";
        }

        private static string Leer(
            XElement elemento,
            string nombre)
        {
            return elemento
                .Elements()
                .FirstOrDefault(x =>
                    x.Name.LocalName == nombre)
                ?.Value ?? "";
        }

        private static int LeerInt(
            XElement elemento,
            string nombre)
        {
            int.TryParse(
                Leer(elemento, nombre),
                out int valor
            );

            return valor;
        }

        private static decimal LeerDecimal(
            XElement elemento,
            string nombre)
        {
            decimal.TryParse(
                Leer(elemento, nombre),
                NumberStyles.Any,
                CultureInfo.InvariantCulture,
                out decimal valor
            );

            return valor;
        }
    }

    public class DeudaVista
    {
        public int Id { get; set; }

        public string Empresa { get; set; } = "";

        public string CI { get; set; } = "";

        public string NombreCompleto { get; set; } = "";

        public int NumeroFactura { get; set; }

        public int Anio { get; set; }

        public string Mes { get; set; } = "";

        public decimal TotalFactura { get; set; }

        public string Estado { get; set; } = "";
    }
}