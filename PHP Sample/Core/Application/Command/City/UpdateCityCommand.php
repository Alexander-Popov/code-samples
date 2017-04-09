<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

final class UpdateCityCommand
{
    /**
     * @var string
     */
    private $id;

    /**
     * @var string
     */
    private $name;

    /**
     * @var string
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
        $this->id = $attributes['id'];
        $this->name = $attributes['name'];
        $this->country = $attributes['country'];
        $this->extraCharge = $attributes['extraCharge'];
    }

    /**
     * @return string
     */
    public function getId() : string
    {
        return $this->id;
    }

    /**
     * @return string
     */
    public function getCountry() : string
    {
        return $this->country;
    }

    /**
     * @return string
     */
    public function getName() : string 
    {
        return $this->name;
    }

    /**
     * @return float
     */
    public function getExtraCharge() : float
    {
        return $this->extraCharge;
    }
}