<?php
declare(strict_types = 1);

namespace app\Core\Application\Dto\City;

use app\Core\Domain\Model\City\City;

final class CityViewDto
{
    public $id;

    /**
     * @var string
     */
    public $name;

    /**
     * @var string
     */
    public $country;

    /**
     * @var int
     */
    public $extraCharge;

    /**
     * CityViewDto constructor.
     * @param City $entity
     */
    public function __construct(City $entity)
    {
        $this->id = $entity->getId();
        $this->name = $entity->getName();
        $this->country = $entity->getCountry();
        $this->extraCharge = $entity->getExtraCharge();
    }
}