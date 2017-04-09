<?php
declare(strict_types = 1);

namespace app\Core\Domain\Model\City;

use app\Core\Domain\Contract\HasId;

final class City implements HasId
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
     * @var int
     */
    private $extraCharge;

    /**
     * City constructor.
     * @param string $id
     * @param string $name
     * @param string $country
     * @param $extraCharge
     */
    private function __construct(
        string $id,
        string $name,
        string $country,
        float $extraCharge
    )
    {
        $this->id = $id;
        $this->name = $name;
        $this->country = $country;
        $this->extraCharge = $extraCharge;
    }

    /**
     * @param string $id
     * @param string $name
     * @param string $country
     * @param $extraCharge
     * @return City
     */
    public static function create(
        string $id,
        string $name,
        string $country,
        float $extraCharge
    ) : City
    {
        $city = new City(
            $id,
            $name,
            $country,
            $extraCharge
        );
        return $city;
    }

    /**
     * @param string $name
     * @param string $country
     */
    public function changePlace(string $name, string $country)
    {
        $this->country = $country;
        $this->name = $name;
    }

    /**
     * @param float $extraCharge
     */
    public function changeExtraCharge(float $extraCharge)
    {
        $this->extraCharge = $extraCharge;
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