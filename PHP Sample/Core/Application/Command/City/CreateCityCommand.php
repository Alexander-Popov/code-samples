<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

use app\Core\Domain\Model\City\City;

final class CreateCityCommand
{
    /**
     * @var City
     */
    public $city;

    /**
     * @var string
     */
    private $name;

    /**
     * @var mixed
     */
    private $country;

    /**
     * @var mixed
     */
    private $extraCharge;

    /**
     * @param array $attributes
     */
    public function __construct(array $attributes)
    {
        $this->name = $attributes['name'];
        $this->country = $attributes['country'];
        $this->extraCharge = $attributes['extraCharge'];
    }

    /**
     * @return string
     */
    public function getName() : string
    {
        return $this->name;
    }

    /**
     * @return string
     */
    public function getCountry() : string
    {
        return $this->country;
    }

    /**
     * @return float
     */
    public function getExtraCharge() : float
    {
        return $this->extraCharge;
    }
}