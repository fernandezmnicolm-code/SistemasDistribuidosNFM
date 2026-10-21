using System.Net.Http.Json;
using System.Text.Json;
using ServidorPagos.Models;
using CoreWCF;

namespace ServidorPagos.Services
{
    public class ServicioPagos : IServicioPagos
    {
        private readonly HttpClient http = new HttpClient();

        private const string SEGIP = "http://localhost:4000/graphql";
        private const string CESSA = "http://localhost:8001/api";
        private const string ELAPAS = "http://localhost:8002/api";

        public Deuda[] verDeuda(
            string CI,
            string primerApellido,
            string segundoApellido,
            string nombres)
        {
            try
            {
                // 1. Verificar identidad en SEGIP
                var consulta = new
                {
                    query = @"query($ci: String!) {
                        persona(CI: $ci) {
                            CI
                            Nombres
                            PrimerApellido
                            SegundoApellido
                        }
                    }",
                    variables = new { ci = CI }
                };

                var respuesta = http.PostAsJsonAsync(
                    SEGIP, consulta
                ).GetAwaiter().GetResult();

                respuesta.EnsureSuccessStatusCode();

                using var documento = JsonDocument.Parse(
                    respuesta.Content.ReadAsStringAsync()
                        .GetAwaiter().GetResult()
                );

                var data = documento.RootElement.GetProperty("data");

                if (data.GetProperty("persona").ValueKind ==
                    JsonValueKind.Null)
                {
                    throw new FaultException(
                     "No existe una persona registrada en SEGIP con ese CI."
                    );
                }

                var persona = data.GetProperty("persona");

                // Verificar que los datos coincidan
                bool correcto =
                    Comparar(persona, "Nombres", nombres) &&
                    Comparar(persona, "PrimerApellido", primerApellido) &&
                    Comparar(persona, "SegundoApellido", segundoApellido);

                if (!correcto)
                {
                    throw new FaultException(
                     "Los nombres o apellidos no coinciden con los registrados en SEGIP."
                    );
                }

                // 2. Consultar facturas pendientes
                var deudas = new List<Deuda>();

                deudas.AddRange(
                    ObtenerFacturas(CESSA, CI, "CESSA")
                );

                deudas.AddRange(
                    ObtenerFacturas(ELAPAS, CI, "ELAPAS")
                );

                return deudas.ToArray();
            }
            catch (Exception ex)
            {
                // Mostrar el detalle tecnico solamente en la terminal del servidor.
                 Console.WriteLine("Error al consultar deudas: " + ex.Message);
 
                 throw new FaultException(
                  "No se pudo completar la consulta. Verifique que SEGIP, CESSA y ELAPAS esten funcionando.",
                 new FaultCode("Receiver")
              );
            }
        }

        private bool Comparar(
            JsonElement persona,
            string campo,
            string valor)
        {
            string recibido =
                persona.GetProperty(campo).GetString() ?? "";

            return string.Equals(
                recibido.Trim(),
                valor.Trim(),
                StringComparison.OrdinalIgnoreCase
            );
        }

        private List<Deuda> ObtenerFacturas(
            string url,
            string ci,
            string empresa)
        {
            var resultado = new List<Deuda>();

            try
            {
                string direccion =
                    $"{url}/facturas/documento/{Uri.EscapeDataString(ci)}";

                var respuesta = http.GetAsync(direccion)
                    .GetAwaiter().GetResult();

                respuesta.EnsureSuccessStatusCode();

                var facturas = respuesta.Content
                    .ReadFromJsonAsync<List<FacturaApi>>()
                    .GetAwaiter().GetResult();

                foreach (var f in facturas ?? new List<FacturaApi>())
                {
                    resultado.Add(new Deuda
                    {
                        Id = f.id,
                        Empresa = empresa,
                        CI = f.numero_documento,
                        NombreCompleto = f.nombre_completo,
                        NumeroFactura = f.numero_factura,
                        Anio = f.anio,
                        Mes = f.mes,
                        TotalFactura = f.total_factura,
                        Estado = f.estado
                    });
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(
                    $"Error consultando {empresa}: {ex.Message}"
                );
                throw;
            }

            return resultado;
        }

        public bool PagarDeuda(Deuda c)
        {
            try
            {
                string url = c.Empresa.ToUpper() switch
                {
                    "CESSA" => CESSA,
                    "ELAPAS" => ELAPAS,
                    _ => ""
                };

                if (string.IsNullOrEmpty(url))
                    return false;

                var respuesta = http.PatchAsync(
                    $"{url}/facturas/{c.Id}/pagar",
                    null
                ).GetAwaiter().GetResult();

                return respuesta.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al pagar: " + ex.Message);
                return false;
            }
        }
    }

    public class FacturaApi
    {
        public int id { get; set; }
        public string numero_documento { get; set; } = "";
        public string nombre_completo { get; set; } = "";
        public int numero_factura { get; set; }
        public int anio { get; set; }
        public string mes { get; set; } = "";
        public decimal total_factura { get; set; }
        public string estado { get; set; } = "";
    }
}