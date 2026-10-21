<?php

namespace Database\Factories;

use App\Models\Factura;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends Factory<Factura>
 */
class FacturaFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'numero_documento' => '147255465',

        'nombre_completo' => fake()->name(),

        'numero_factura' => fake()->numberBetween(1000, 9999),

        'anio' => 26,

        'mes' => fake()->randomElement([
            'Enero',
            'Febrero',
            'Marzo',
            'Abril',
            'Mayo',
            'Junio',
            'Julio',
            'Agosto',
            'Septiembre',
            'Octubre',
            'Noviembre',
            'Diciembre'
        ]),

        'total_factura' => fake()->randomFloat(2, 20, 1000),

        'estado' => fake()->randomElement([
            'emitida',
            'pagada',
            'anulada'
        ]),
    ];
    }
}
